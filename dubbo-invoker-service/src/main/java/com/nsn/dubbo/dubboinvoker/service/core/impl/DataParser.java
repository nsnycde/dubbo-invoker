package com.nsn.dubbo.dubboinvoker.service.core.impl;

import com.alibaba.fastjson.JSON;
import com.nsn.dubbo.dubboinvoker.kit.ClassLoaderKit;
import com.nsn.dubbo.dubboinvoker.kit.convert.Converter;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.util.*;

/**
 * @author nsn
 */
@Component
@Data
public class DataParser {

    /**
     * 对象开始 @
     */
    private static final String DESC_OBJECT_START = "@";
    /**
     * 对象结束 #
     */
    private static final String DESC_OBJECT_END = "#";
    /**
     * 描述符表面后跟字段为List  开始位置
     */
    private static final String DESC_LIST = ">";

    /**
     * 构建name行
     * @param method
     * @return
     */
    public List<String> buildNameRow(Method method){
        List<String> nameRow = new ArrayList<>();
        Class<?>[] paramClassArray = method.getParameterTypes();
        Type[] genericArray = method.getGenericParameterTypes();
        for(int i=0;i<paramClassArray.length;i++){
            this.buildNameRow(nameRow, paramClassArray[i], "arg"+i, genericArray[i].getTypeName());
        }
        return nameRow;
    }

    private void buildNameRow(List<String> nameRow, Class<?> clz, String name, String genericInfo){
        //list
        if(clz.isAssignableFrom(List.class)){
            nameRow.add(">1");
            clz = this.getGenericClass(genericInfo);
        }
        //基本类型
        if(Converter.hasConvert(clz)){
            nameRow.add(name);
        }else{
            //对象
            nameRow.add("@" + name);
            List<Field> fields = new ArrayList<>();
            //先添加父类字段，再添加当前类字段
            fields.addAll(Arrays.asList(clz.getSuperclass().getDeclaredFields()));
            fields.addAll(Arrays.asList(clz.getDeclaredFields()));
            for(Field field : fields){
                if(Modifier.isFinal(field.getModifiers())){
                    continue;
                }
                this.buildNameRow(nameRow, field.getType(), field.getName(), field.getGenericType().getTypeName());
            }
            nameRow.add("#" + name);
        }
    }

    /**
     * 构建name行
     * @param method
     * @return
     */
    public List<List<String>> buildDataRow(Method method, String json){
        List<Object> params = JSON.parseObject(json, List.class);
        List<List<String>> dataRow = new ArrayList<>();
        Class<?>[] paramClassArray = method.getParameterTypes();
        Type[] genericArray = method.getGenericParameterTypes();
        for(int i=0;i<paramClassArray.length;i++){
            this.buildDataRow(dataRow, 0, 0, paramClassArray[i], genericArray[i].getTypeName(), params.size() > i ? params.get(i) : null);
        }
        return dataRow;
    }

    private void buildDataRow(List<List<String>> dataRow, int rowIndex, int columnIndex, Class<?> clz, String genericInfo, Object value){
        try {
            if(dataRow.size() <= rowIndex){
                dataRow.add(new ArrayList<>());
            }
            while(dataRow.get(rowIndex).size() < columnIndex){
                dataRow.get(rowIndex).add(null);
            }
            //基本类型
            if(Converter.hasConvert(clz)){
                if(value == null){
                    dataRow.get(rowIndex).add(null);
                }else{
                    dataRow.get(rowIndex).add(String.valueOf(value));
                }
            }else if(clz.isAssignableFrom(List.class)){
                //list
                dataRow.get(rowIndex).add(null);
                clz = this.getGenericClass(genericInfo);
                List<Object> params = value == null ? null : JSON.parseObject(JSON.toJSONString(value), List.class);
                int size = params == null ? 1 : params.size();
                int currentColumnIndex = dataRow.get(rowIndex).size();
                for (int i=0;i<size;i++){
                    this.buildDataRow(dataRow, rowIndex+i, currentColumnIndex, clz, null, params == null ? null : params.get(i));
                }
            }else{
                //对象
                Object obj = value == null ? null : JSON.parseObject(JSON.toJSONString(value), clz);
                dataRow.get(rowIndex).add(null);
                List<Field> fields = new ArrayList<>();
                //先添加父类字段，再添加当前类字段
                fields.addAll(Arrays.asList(clz.getSuperclass().getDeclaredFields()));
                fields.addAll(Arrays.asList(clz.getDeclaredFields()));
                for(Field field : fields){
                    if(Modifier.isFinal(field.getModifiers())){
                        continue;
                    }
                    field.setAccessible(true);
                    this.buildDataRow(dataRow, rowIndex, columnIndex, field.getType(), field.getGenericType().getTypeName(), obj == null ? null : field.get(obj));
                }
                dataRow.get(rowIndex).add(null);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取泛型类
     * @param genericInfo
     * @return
     */
    private Class<?> getGenericClass(String genericInfo){
        String genericClassName = genericInfo.substring(genericInfo.indexOf("<") + 1, genericInfo.indexOf(">"));
        return ClassLoaderKit.forName(genericClassName);
    }

    public List<Object> parseRow(List<List<String>> tableData){
        List<String> nameRow = tableData.get(0);
        List<String> dataRow = tableData.get(1);
        return new ArrayList<>(this.parseObject(0, nameRow, dataRow, tableData).getDataMap().values());
    }

    private TableInfo parseObject(int columnIndex, List<String> nameRow, List<String> dataRow, List<List<String>> tableData){
        //返回info
        TableInfo tableInfo = new TableInfo();
        Map<String, Object> dataMap = new LinkedHashMap<>();
        tableInfo.setDataMap(dataMap);

        TableInfo currentTableInfo = null;
        //解析
        while(columnIndex < nameRow.size()){
            String name = nameRow.get(columnIndex);
            //List
            if(name.startsWith(DESC_LIST)){
                //取实际字段列
                columnIndex++;
                String listName = nameRow.get(columnIndex);
                //获取行数
                int lineNum = Integer.parseInt(name.substring(DESC_LIST.length()));
                //往下探测
                List<String> currentDataRow = dataRow;
                List<Object> valueList = new ArrayList<>();
                while(lineNum-->0){
                    //解析行
                    if(listName.startsWith(DESC_OBJECT_START)){
                        currentTableInfo = this.parseObject(columnIndex + 1, nameRow, currentDataRow, tableData);
                        valueList.add(currentTableInfo.getDataMap());
                        currentDataRow = this.nextRow(tableData, currentDataRow, currentTableInfo.getRowNum());
                    }else {
                        valueList.add(dataRow.get(columnIndex));
                        currentDataRow = this.nextRow(tableData, currentDataRow, 1);
                    }
                }

                if(currentTableInfo != null){
                    //若list为对象，则取对象处理后的index
                    columnIndex = currentTableInfo.getColumnIndex();
                    tableInfo.setRowNum(Math.max(valueList.size(), currentTableInfo.getRowNum()));
                    dataMap.put(listName.substring(DESC_OBJECT_START.length()), valueList);
                }else{
                    //基本类型
                    dataMap.put(listName, valueList);
                    tableInfo.setRowNum(valueList.size());
                }
            }else{
                //Object
                if(name.startsWith(DESC_OBJECT_START)){
                    currentTableInfo = this.parseObject(columnIndex + 1, nameRow, dataRow, tableData);
                    dataMap.put(name.substring(DESC_OBJECT_START.length()), currentTableInfo.getDataMap());
                    columnIndex = currentTableInfo.getColumnIndex();
                }else if(name.startsWith(DESC_OBJECT_END)){
                    break;
                }else{
                    //基本值
                    dataMap.put(name, dataRow.size() > columnIndex ? dataRow.get(columnIndex) : null);
                }
            }
            columnIndex++;
        }
        tableInfo.setColumnIndex(columnIndex);
        return tableInfo;
    }

    /**
     * 获取下一指定行
     * @param dataRow
     * @param increment
     * @return
     */
    private List<String> nextRow(List<List<String>> tableData, List<String> dataRow, int increment){
        return tableData.get(tableData.indexOf(dataRow) + increment);
    }

    /**
     * 每次递归构造的信息
     */
    @Data
    private static class TableInfo {
        /**
         * 每次递归返回的数据
         */
        private Map<String,Object> dataMap;
        /**
         * 当次解析后 columnIndex
         */
        private int columnIndex;
        /**
         * 当次解析 占用的行 默认占用行为1
         */
        private int rowNum = 1;
    }

    public static void main(String[] args) {
        List<Object> list = new ArrayList<>();
        list.add(21);
        Info info = new Info();
        info.setAge(31);
        info.setName("呵呵哒");
        List<String> innerList = Arrays.asList("1","2","3");
        info.setSubject(innerList);
        list.add(info);
        list.add(innerList);
        Method method = null;
        try {
            method = DataParser.class.getDeclaredMethod("test", int.class, Info.class, List.class);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }

        System.out.println(JSON.toJSONString(new DataParser().buildNameRow(method)));
        List<List<String>> result = new DataParser().buildDataRow(method, JSON.toJSONString(list));
        result.forEach(item -> System.out.println(JSON.toJSONString(item)));
    }


    public void test(int age, Info info, List<String> list){
        System.out.println(JSON.toJSONString(age));
        System.out.println(JSON.toJSONString(info));
    }

    @Data
    public static class Info {

        private String name;
        private int age;
        private List<String> subject;
    }
}
