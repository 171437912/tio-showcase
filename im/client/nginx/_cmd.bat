echo off
echo -
echo #����Դ����
echo mvn dependency:sources
echo #����Դ����jar�� -DdownloadJavadocs=true ����javadoc��
echo -DdownloadSources=true

echo -
echo #��jar��ѹ����
echo mvn dependency:unpack-dependencies
echo #��jar������ĳһĿ¼��(����jar��ͬһĿ¼��)
echo mvn dependency:copy-dependencies -Dmdep.useRepositoryLayout=false
echo #��jar���ֿ�Ŀ¼��������()
echo mvn dependency:copy-dependencies -Dmdep.useRepositoryLayout=true -Dmdep.copyPom=true

echo -
echo #Checking for new versions of dependencies
echo mvn versions:display-dependency-updates
echo mvn versions:set -DnewVersion=4.0.0-talent-999

cmd
