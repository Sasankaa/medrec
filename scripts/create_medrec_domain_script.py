# Create the medrec sample domain
wlhome = os.environ["WL_HOME"]
mwhome = os.environ["MW_HOME"]

import os
from weblogic.management.configuration import TargetMBean

wl_home                  = os.environ.get("WL_HOME")
domain_path              = os.environ.get("DOMAIN_HOME")
domain_name              = "medrec"
username                 = "weblogic"
password                 = "welcome1"
admin_server_name        = "admin-server"
admin_port               = 7011
t3_channel_port          = 30012
t3_public_address        = "kubernetes"
cluster_name             = "cluster-medrec"
number_of_ms             = 5
managed_server_name_base = "managed-server"
managed_server_port      = 8001

readTemplate(wl_home + "/common/templates/wls/wls.jar")

cmo.setName(domain_name)
setOption("DomainName", domain_name)
setOption("OverwriteDomain", "true")

cd("/Security/" + domain_name + "/User/weblogic")
cmo.setName(username)
cmo.setPassword(password)

cd("/Servers/AdminServer")
cmo.setListenPort(admin_port)
cmo.setName(admin_server_name)
nap=create("T3Channel", 'NetworkAccessPoint')
nap.setPublicPort(t3_channel_port)
nap.setPublicAddress(t3_public_address)
nap.setListenPort(t3_channel_port)

cd("/")
cl=create(cluster_name, 'Cluster')
templateName = cluster_name + "-template"
st=create(templateName, "ServerTemplate")
st.setListenPort(managed_server_port)
st.setCluster(cl)
cd("/Clusters/" + cluster_name)
ds=create(cluster_name, "DynamicServers")
ds.setServerTemplate(st)
ds.setServerNamePrefix(managed_server_name_base)
ds.setDynamicClusterSize(number_of_ms)
ds.setMaxDynamicClusterSize(number_of_ms)
ds.setCalculatedListenPorts(false)

writeDomain(mwhome + '/user_projects/domains/medrec')
closeTemplate()

readDomain(mwhome + '/user_projects/domains/medrec')

cmo.setProductionModeEnabled(true)

setOption('ReplaceDuplicates','false')
# setOption('AppDir','@WL_HOME/samples/server/medrec')
setOption('BackupFiles','false')

# Apply Medrec template
# ==================
print 'Add Medrec template'
addTemplate(wlhome + '/common/templates/wls/medrec.jar')

# Creating a Transactional Data Source
# ==================
#print 'Create Derby Transactional Data Source'
#dsname = 'MedRecGlobalDataSourceXA'
#dsjndiname = 'jdbc/MedRecGlobalDataSourceXA'
#dsdriver = 'org.apache.derby.jdbc.ClientXADataSource'
#dsurl = 'jdbc:derby://localhost:1527/medrec'
#dspassword = 'medrec'
#dsusername = 'medrec'
#dsinitialcapacity = 2
#cd('/')
#jdbcXASR = create("MedRecGlobalDataSourceXA","JDBCSystemResource")
#cd('JDBCSystemResource/MedRecGlobalDataSourceXA/JdbcResource/MedRecGlobalDataSourceXA')
#connectionPoolParams = create('connectionPoolParams', 'JDBCConnectionPoolParams')
#connectionPoolParams.setInitialCapacity(2)
#connectionPoolParams.setMaxCapacity(10)
#connectionPoolParams.setCapacityIncrement(1)
#connectionPoolParams.setShrinkFrequencySeconds(900)
#connectionPoolParams.setTestConnectionsOnReserve(1)
#connectionPoolParams.setTestTableName("SYSTABLES")
#driverParams = create('driverParams', 'JDBCDriverParams')
#driverParams.setDriverName("org.apache.derby.jdbc.ClientXADataSource")
#driverParams.setUrl("jdbc:derby://localhost:1527/demo")
#driverParams.setPasswordEncrypted("medrec")
#cd('JDBCDriverParams/NO_NAME_0')
#create('medrec','Properties')
#cd('Properties/NO_NAME_0')
#create('user', 'Property')
#cd('Property/user')
#cmo.setValue('medrec')
#cd('../..')
#create('DatabaseName', 'Property')
#cd('Property/DatabaseName')
#cmo.setValue('demo')
#cd('../../../../../..')
#dsXAParams = create('dataSourceParams', 'JDBCDataSourceParams')
#cd('JDBCDataSourceParams/NO_NAME_0')
#set('JNDIName', ['jndi/MedRecGlobalDataSourceXA'])


print 'Create Oracle Transactional Data Source'
dsname = 'MedRecGlobalDataSourceXA'
dsjndiname = 'jdbc/MedRecGlobalDataSourceXA'
dsdriver = 'oracle.jdbc.OracleDriver'
dsurl = 'jdbc:oracle:thin:@db1pnagy_high'
dspassword = 'tactful@10Cr'
dsusername = 'medrec'
cd('/')
jdbcXASR = create(dsname,"JDBCSystemResource")
cd('JDBCSystemResource/' + dsname + '/JdbcResource/' + dsname + '')
connectionPoolParams = create('connectionPoolParams', 'JDBCConnectionPoolParams')
connectionPoolParams.setInitialCapacity(2)
connectionPoolParams.setMaxCapacity(10)
connectionPoolParams.setCapacityIncrement(1)
connectionPoolParams.setShrinkFrequencySeconds(900)
connectionPoolParams.setTestConnectionsOnReserve(1)
connectionPoolParams.setTestTableName("SQL ISVALID")
driverParams = create('driverParams', 'JDBCDriverParams')
driverParams.setDriverName(dsdriver)
driverParams.setUrl(dsurl)
driverParams.setPasswordEncrypted(dspassword)
cd('JDBCDriverParams/NO_NAME_0')
create('medrec','Properties')
cd('Properties/NO_NAME_0')

create('oracle.net.tns_admin', 'Property')
cd('Property/oracle.net.tns_admin')
cmo.setValue('/u01/oracle/wallet')

cd('../..')
create('oracle.net.ssl_version', 'Property')
cd('Property/oracle.net.ssl_version')
cmo.setValue('1.2')

cd('../..')
create('javax.net.ssl.trustStore', 'Property')
cd('Property/javax.net.ssl.trustStore')
cmo.setValue('/u01/oracle/wallet/truststore.jks')

cd('../..')
create('oracle.net.ssl_server_dn_match', 'Property')
cd('Property/oracle.net.ssl_server_dn_match')
cmo.setValue('true')

cd('../..')
create('user', 'Property')
cd('Property/user')
cmo.setValue(dsusername)

cd('../..')
create('javax.net.ssl.keyStoreType', 'Property')
cd('Property/javax.net.ssl.keyStoreType')
cmo.setValue('JKS')

cd('../..')
create('javax.net.ssl.trustStoreType', 'Property')
cd('Property/javax.net.ssl.trustStoreType')
cmo.setValue('JKS')

cd('../..')
create('javax.net.ssl.keyStore', 'Property')
cd('Property/javax.net.ssl.keyStore')
cmo.setValue('/u01/oracle/wallet/keystore.jks')

#cd('../..')
#create('javax.net.ssl.keyStorePassword', 'Property')
#cd('Property/javax.net.ssl.keyStorePassword')
#cmo.setValue('Ud08bEsNiX92D5I')

#cd('../..')
#create('javax.net.ssl.trustStorePassword', 'Property')
#cd('Property/javax.net.ssl.trustStorePassword')
#cmo.setValue('Ud08bEsNiX92D5I')

cd('../..')
create('oracle.jdbc.fanEnabled', 'Property')
cd('Property/oracle.jdbc.fanEnabled')
cmo.setValue('false')

cd('../../../../../..')
dsXAParams = create('dataSourceParams', 'JDBCDataSourceParams')
cd('JDBCDataSourceParams/NO_NAME_0')
set('JNDIName', ['jndi/MedRecGlobalDataSourceXA'])

# Assign datasource to target
# ======
assign('JDBCSystemResource', dsname, 'Target', admin_server_name)
assign('JDBCSystemResource', dsname, 'Target', cluster_name)

# Create JMS resources
# ======
print 'Create a JMS Resources.'
JMSServerName = 'MedRecJMSServer'
JMSModuleName = 'MedRec-jms'
JMSSubdeploymentName = 'medrecSub'
cfName = 'MedRecConnectionFactory'
cfJndiName = 'com.oracle.medrec.jms.connectionFactory'
queueName = 'PatientNotificationQueue'
queueJndiName = 'com.oracle.medrec.jms.PatientNotificationQueue'

print 'Create JMS Server.'
cd('/')
jmsServer = create(JMSServerName,'JMSServer')
assign('JMSServer', JMSServerName, 'Target', cluster_name)
#assign(sourceType, sourceName, destinationType, destinationName)

print 'Create JMS Module.'
cd('/')
jmsModule = create(JMSModuleName,'JMSSystemResource')
assign('JMSSystemResource', JMSModuleName, 'Target', cluster_name)
cd('JMSSystemResource/' + JMSModuleName + '/JmsResource/NO_NAME_0')

print 'Create JMS DistributedQueue.'
myq = create(queueName, 'UniformDistributedQueue')
myq.setJNDIName(queueJndiName)
myq.setSubDeploymentName(JMSSubdeploymentName)

print 'Create JMS ConnectionFactory.'
mycf = create(cfName, 'ConnectionFactory')
mycf.setJNDIName(cfJndiName)
mycf.setSubDeploymentName(JMSSubdeploymentName)

print 'Create JMS Subdeployment.'
cd('/JMSSystemResource/' + JMSModuleName + '')
create(JMSSubdeploymentName, 'SubDeployment')

cd('/')
assign('JMSSystemResource.SubDeployment', JMSModuleName + '.' + JMSSubdeploymentName, 'Target', JMSServerName)

updateDomain()
closeDomain()
exit()
