echo off
echo -
echo #����Դ����
echo mvn dependency:sources
echo -

echo #����Դ����jar�� -DdownloadJavadocs=true ����javadoc��
echo -DdownloadSources=true
echo -
echo -



echo #��jar��ѹ����
echo mvn dependency:unpack-dependencies
echo -

echo #��jar������ĳһĿ¼��(����jar��ͬһĿ¼��)
echo mvn dependency:copy-dependencies -Dmdep.useRepositoryLayout=false
echo -

echo #��jar���ֿ�Ŀ¼��������()
echo mvn dependency:copy-dependencies -Dmdep.useRepositoryLayout=true -Dmdep.copyPom=true
echo -
echo -



echo #���汾����
echo mvn versions:display-dependency-updates
echo -

echo #�汾���
echo mvn versions:set -DnewVersion=4.0.0-talent-999
echo -


call mvn versions:display-dependency-updates
pause