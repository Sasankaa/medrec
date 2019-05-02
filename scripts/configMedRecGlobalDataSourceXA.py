"""
This script configures JDBC data source MedRecGlobalDataSourceXA and redeploys it
to the server
"""

driverUrl=sys.argv[5]
driverName=sys.argv[6]
driverUser=sys.argv[7]
driverPassword=sys.argv[8]
testSql=sys.argv[9]

url='t3://'+sys.argv[3]+':'+sys.argv[4]
print 'connect to the server'
try:
    connect(sys.argv[1], sys.argv[2], url)
except WLSTException:
    print "*******************************************************************************************"
    print "*** Reason: The server is off now, please start up the server and run the command again ***"
    print "*******************************************************************************************"
    # exits system, and tell ant the error code
    exit(exitcode=101)

edit()
startEdit()
# start edit
cd("JDBCSystemResources/MedRecGlobalDataSourceXA/JDBCResource/MedRecGlobalDataSourceXA/JDBCConnectionPoolParams/MedRecGlobalDataSourceXA")
poolParams=cmo
cd("../..")
cd("JDBCDriverParams/MedRecGlobalDataSourceXA")
jdbcDriverParams=cmo
cd("Properties/MedRecGlobalDataSourceXA/Properties/user")
user=cmo
print 'set test table name: ' + testSql
poolParams.setTestTableName('SQL ' + testSql)
print 'set driver name: ' + driverName
jdbcDriverParams.setDriverName(driverName)
print 'set driver url: ' + driverUrl
jdbcDriverParams.setUrl(driverUrl)
print 'set driver password: ' + driverPassword
jdbcDriverParams.setPassword(driverPassword)
print 'set driver user: ' + driverUser
user.setValue(driverUser)

save()
activate(block="true")

print 'Done modifying the data source'

dsname = 'MedRecGlobalDataSourceXA'

# Create Datasource
# ==================
cd("/")
create(dsname, 'JDBCSystemResource')
cd('/JDBCSystemResource/' + dsname + '/JdbcResource/' + dsname)
cmo.setName(dsname)

cd('/JDBCSystemResource/' + dsname + '/JdbcResource/' + dsname)
create('myJdbcDataSourceParams','JDBCDataSourceParams')
cd('JDBCDataSourceParams/NO_NAME_0')
set('JNDIName', java.lang.String(dsjndiname))
set('GlobalTransactionsProtocol', java.lang.String('None'))

cd('/JDBCSystemResource/' + dsname + '/JdbcResource/' + dsname)
create('myJdbcDriverParams','JDBCDriverParams')
cd('JDBCDriverParams/NO_NAME_0')
set('DriverName', dsdriver)
set('URL', dsurl)
set('PasswordEncrypted', dspassword)
set('UseXADataSourceInterface', 'false')

print 'create JDBCDriverParams Properties'
create('myProperties','Properties')
cd('Properties/NO_NAME_0')
create('user','Property')
cd('Property/user')
set('Value', dsusername)

print 'create JDBCConnectionPoolParams'
cd('/JDBCSystemResource/' + dsname + '/JdbcResource/' + dsname)
create('myJdbcConnectionPoolParams','JDBCConnectionPoolParams')
cd('JDBCConnectionPoolParams/NO_NAME_0')
set('TestTableName','SQL SELECT 1 FROM DUAL')
set('InitialCapacity', int(dsinitialcapacity))

# Assign
# ======
assign('JDBCSystemResource', dsname, 'Target', admin_server_name)
assign('JDBCSystemResource', dsname, 'Target', cluster_name)
