package com.nsn.dubbo.dubboinvoker.kit;

import lombok.extern.slf4j.Slf4j;
import org.apache.maven.repository.internal.MavenRepositorySystemUtils;
import org.eclipse.aether.DefaultRepositorySystemSession;
import org.eclipse.aether.RepositorySystem;
import org.eclipse.aether.artifact.DefaultArtifact;
import org.eclipse.aether.collection.CollectRequest;
import org.eclipse.aether.connector.basic.BasicRepositoryConnectorFactory;
import org.eclipse.aether.graph.Dependency;
import org.eclipse.aether.graph.DependencyNode;
import org.eclipse.aether.impl.DefaultServiceLocator;
import org.eclipse.aether.repository.LocalRepository;
import org.eclipse.aether.repository.RemoteRepository;
import org.eclipse.aether.resolution.ArtifactRequest;
import org.eclipse.aether.resolution.ArtifactResult;
import org.eclipse.aether.spi.connector.RepositoryConnectorFactory;
import org.eclipse.aether.spi.connector.transport.TransporterFactory;
import org.eclipse.aether.transport.file.FileTransporterFactory;
import org.eclipse.aether.transport.http.HttpTransporterFactory;
import org.springframework.util.CollectionUtils;
import org.springframework.util.FileSystemUtils;

import java.io.File;
import java.util.*;

/**
 * 描述: 依赖关系处理器
 * @author nsn
 */
@Slf4j
public class MavenKit {

    /**
     * maven local repo path
     */
    private final static String MAVEN_LOCAL_REPO_PATH = System.getProperty("user.dir") + "/localRepo";

    /**
     * maven下载相关
     */
    private final static RepositorySystem REPOSITORY_SYSTEM ;
    private final static DefaultRepositorySystemSession SESSION;
    private final static List<RemoteRepository> REPOSITORY_LIST;
    private final static LocalRepository LOCAL_REPOSITORY;

    static {
        DefaultServiceLocator locator = MavenRepositorySystemUtils.newServiceLocator();
        locator.addService(RepositoryConnectorFactory.class, BasicRepositoryConnectorFactory.class);
        locator.addService(TransporterFactory.class, FileTransporterFactory.class);
        locator.addService(TransporterFactory.class, HttpTransporterFactory.class);
        REPOSITORY_SYSTEM = locator.getService(RepositorySystem.class);
        LOCAL_REPOSITORY = new LocalRepository(MAVEN_LOCAL_REPO_PATH);
        SESSION = MavenRepositorySystemUtils.newSession();
        SESSION.setLocalRepositoryManager(REPOSITORY_SYSTEM.newLocalRepositoryManager(SESSION, LOCAL_REPOSITORY));
        REPOSITORY_LIST = new ArrayList<>();
    }

    /**
     * 解析maven配置
     * @param nexusUrlList  私服仓库
     * @param dependencyList  依赖字符串
     * @param deleteJarList 删除的jarList
     * @param deleteRepo  是否删除本地仓库  true 删除  false 不删除
     * @return jarPath set
     */
    public static Set<String> analyse(Collection<String> nexusUrlList, Collection<String> dependencyList, List<String> deleteJarList, boolean deleteRepo){
        if(CollectionUtils.isEmpty(nexusUrlList)
                || CollectionUtils.isEmpty(dependencyList)){
            return null;
        }
        //构建远程仓库
        REPOSITORY_LIST.clear();
        nexusUrlList.forEach(repositoryUrl -> REPOSITORY_LIST.add(new RemoteRepository.Builder( null, "default", repositoryUrl).build()));

        if(deleteRepo){
            log.info("删除本地仓库...");
            FileSystemUtils.deleteRecursively(new File(MAVEN_LOCAL_REPO_PATH));
        }else if(!CollectionUtils.isEmpty(deleteJarList)){
            log.info("删除指定jarList...");
            deleteJarList.forEach(jarPath ->
                FileSystemUtils.deleteRecursively(new File(jarPath).getParentFile())
            );
        }
        //进行下载
        return downloadJar(dependencyList);
    }

    private static Set<String> downloadJar(Collection<String> dependencyList) {
        Set<String> filePathSet = new HashSet<>();
        log.info("开始检查依赖...");
        dependencyList.forEach(dependencyStr -> {
            try {
                Dependency dependency = new Dependency(new DefaultArtifact(dependencyStr),null);
                CollectRequest collectRequest = new CollectRequest();
                collectRequest.setRoot(dependency);
                collectRequest.setRepositories(REPOSITORY_LIST);
                //根节点
                DependencyNode root = REPOSITORY_SYSTEM.collectDependencies(SESSION, collectRequest).getRoot();
                //递归
                List<DependencyNode> nodeList = new ArrayList<>();
                processNode(nodeList, root);
                //循环下载jar
                for(DependencyNode node : nodeList){
                    ArtifactRequest artifactRequest = new ArtifactRequest();
                    artifactRequest.setRepositories(REPOSITORY_LIST);
                    artifactRequest.setDependencyNode(node);
                    ArtifactResult result = REPOSITORY_SYSTEM.resolveArtifact(SESSION, artifactRequest);
                    //放入类加载器
                    filePathSet.add(result.getArtifact().getFile().getAbsolutePath());
                }
            } catch (Exception e) {
                log.error("出现无法解析的依赖!dependencyStr:{}", dependencyStr);
            }
        });
        log.info("依赖处理结束...");
        return filePathSet;
    }

    private static void processNode(List<DependencyNode> nodeList, DependencyNode root){
        nodeList.add(root);
        for(DependencyNode node : root.getChildren()){
            processNode(nodeList, node);
        }
    }

}
