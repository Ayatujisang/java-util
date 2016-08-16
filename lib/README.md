# 依赖jar包

pinyin4j

<dependency>
    <groupId>net.sourceforge.pinyin4j</groupId>
    <artifactId>pinyin4j</artifactId>
    <version>2.5.0</version>
</dependency>


发布jar包到私服
mvn deploy:deploy-file -DgroupId=net.sourceforge.pinyin4j  -DartifactId=pinyin4j -Dversion=2.5.0 -Dpackaging=jar -Dfile=pinyin4j-2.5.0.jar  -DrepositoryId=releases -Durl=http://mvn.charm.com/nexus/content/repositories/thirdparty/