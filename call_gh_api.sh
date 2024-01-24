#! /bin/bash 


if [ -z "$6" ]
then 
    echo "usage: bash $0 $1[git_user] $2[git_pass] $3[jk_job] $4[build_num] $5[state] $6[description]"
    exit 1
fi 

GIT_USER=$1
GIT_PASS=$2
JOB=$3
NUMBER=$4
STATE=$5
DESC=$6

echo "git_user git_pass $3 $4 $5 $6"

API_SERVER="api.github.com"
JENKINS_URL="jk.enxoa.com"
CONTEXT="ci/jenkins/config-check"


gitRepoUrl=$(git config --get remote.origin.url)
gitRepoName=$(echo $gitRepoUrl | awk -F/ '{print $NF}' | sed 's@.git@@g')
gitCMSHA=$(git rev-parse HEAD)

ACCEPT="Accept: application/vnd.github+json"
TOKEN="Authorization: Bearer ${GIT_PASS}"
API_VER="X-GitHub-Api-Version: 2022-11-28"

TARGET_URL="https://${JENKINS_URL}/job/${JOB}/${NUMBER}/console"
STATUS_JSON='{"state":"%s","target_url":"%s","description":"%s","context":"%s"}'
STATUS_PAYLOAD=$(printf "$STATUS_JSON" "$STATE" "$TARGET_URL" "$DESC" "$CONTEXT")

curl -L -X POST -H "Accept: application/vnd.github+json" -H "Authorization: Bearer ${GIT_PASS}" -H "X-GitHub-Api-Version: 2022-11-28" \
https://${API_SERVER}/repos/${GIT_USER}/${gitRepoName}/statuses/${gitCMSHA} \
-d "$STATUS_PAYLOAD"

echo "-- https://${API_SERVER}/repos/${GIT_USER}/${gitRepoName}/statuses/${gitCMSHA} --"
