# Create the medrec sample domain
wlhome = os.environ["WL_HOME"]
mwhome = os.environ["MW_HOME"]
readTemplate(wlhome + '/common/templates/wls/wls.jar')

setOption('OverwriteDomain','true')
setOption('BackupFiles','false')

cd('/Server/AdminServer')
cmo.setName('MedRecServer')
cmo.setListenPort(7011)

ssl=create('MedRecServer', 'SSL')
ssl.setEnabled(1)
ssl.setListenPort(7012)

cd('/')
cd('/Security/base_domain/User/weblogic')
set('Name','weblogic')
cmo.setPassword('welcome1')

writeDomain(mwhome + '/user_projects/domains/medrec')
closeTemplate()

readDomain(mwhome + '/user_projects/domains/medrec')

setOption('ReplaceDuplicates','false')
# setOption('AppDir','@WL_HOME/samples/server/medrec')
setOption('BackupFiles','false')

addTemplate(wlhome + '/common/templates/wls/medrec.jar')


updateDomain()
closeDomain()
exit()
