cmake_minimum_required(VERSION 2.6)
#set(SUPER_VERSION 3.2.2)

set(CMAKE_INSTALL_PREFIX ${CMAKE_CURRENT_SOURCE_DIR})
set(PLATFORM_RPM_REFLEX_DIR ${CMAKE_CURRENT_SOURCE_DIR})

execute_process(COMMAND mkdir -p ${CMAKE_INSTALL_PREFIX})
execute_process(COMMAND rm -rf ${CMAKE_INSTALL_PREFIX}/java ${CMAKE_INSTALL_PREFIX}/doc)

add_custom_target(application ALL
            COMMAND mvn clean install -Dmaven.test.skip=true
            WORKING_DIRECTORY ${CMAKE_CURRENT_SOURCE_DIR})

#install(CODE "execute_process(COMMAND cp -r ${CMAKE_CURRENT_SOURCE_DIR}/target/kafka-connect-hdfs-${SUPER_VERSION}-package/share/java ${CMAKE_INSTALL_PREFIX}/)")
#install(CODE "execute_process(COMMAND cp -r ${CMAKE_CURRENT_SOURCE_DIR}/target/kafka-connect-hdfs-${SUPER_VERSION}-package/share/doc ${CMAKE_INSTALL_PREFIX}/)")

add_custom_target(kafka_connect_hdfs_csv_rpm
                   WORKING_DIRECTORY ${CMAKE_INSTALL_PREFIX}
                   COMMAND /usr/local/bin/make-rpm.sh
                   -s ${CMAKE_CURRENT_SOURCE_DIR}/rpm_conf/confluent-kafka-connect-hdfs.spec
                   -r ${CMAKE_INSTALL_PREFIX}
                   -o ${PLATFORM_RPM_REFLEX_DIR}
                   -c ${CMAKE_CURRENT_SOURCE_DIR}/rpm_conf/build_version.sh
                   VERBATIM)


