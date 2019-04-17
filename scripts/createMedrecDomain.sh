#!/bin/sh

# Users must set $MW_HOME variable
if [ -z "${MW_HOME}" ] || [ ! -d "${MW_HOME}" ]; then
  echo "ERROR: You must set MW_HOME and it must point to a directory".
  echo "       where an installation of WebLogic exists."
  exit 1;
fi

# Users must set $JAVA_HOME variable
if [ -z "${JAVA_HOME}" ] || [ ! -d "${JAVA_HOME}/bin" ]; then
  echo "ERROR: You must set JAVA_HOME and point it to a valid location"
  echo "       of where your JDK has been installed"
  exit 1;
fi

# Setup the WLS environment
. ${MW_HOME}/wlserver/server/bin/setWLSEnv.sh

export WL_HOME=$MW_HOME/wlserver

${MW_HOME}/oracle_common/common/bin/wlst.sh create_medrec_domain_script.py
