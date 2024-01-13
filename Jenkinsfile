pipeline {
    options {
        buildDiscarder(logRotator(numToKeepStr: '5', artifactNumToKeepStr: '5'))
    }
    parameters {
        string(name: 'VERSION', defaultValue: '', description: '')
        booleanParam(name: 'RELEASE', defaultValue: false, description: '')
        booleanParam(name: 'ARTIFACTORY', defaultValue: false, description: '')
        booleanParam(name: 'INIT_DEPLOYMENT', defaultValue: false, description: '')
    }
    environment {
        HARBOR_URL = 'core.harbor.rjst.de'
        HARBOR_PREFIX = 'dev'
        ARTIFACTORY_URL = 'https://artifactory.rjst.de/artifactory'
        ARTIFACTORY_REPO_NAME = 'maven-local-rjst'
        VERSION = ''
        NAME = ''
    }
    agent {
        kubernetes {
            yaml """
        apiVersion: v1
        kind: Pod
        spec:
          containers:
          - name: kubectl
            image: joshendriks/alpine-k8s
            command:
            - /bin/cat
            tty: true
          - name: maven
            image: maven:3.8.8-eclipse-temurin-21
            command: ["cat"]
            tty: true
            volumeMounts:
              - name: maven-secret
                mountPath: /usr/maven
          - name: kaniko
            image: gcr.io/kaniko-project/executor:debug
            command:
            - /busybox/cat
            tty: true
            volumeMounts:
              - name: kaniko-secret
                mountPath: /kaniko/.docker
          - name: dind
            image: docker:dind
            tty: true
            env:
              - name: DOCKER_TLS_CERTDIR
                value: ""
            securityContext:
              privileged: true
          volumes:
            - name: kaniko-secret
              secret:
                secretName: regcred
                items:
                  - key: .dockerconfigjson
                    path: config.json
            - name: maven-secret
              secret:
                secretName: maven-settings-secret
                items:
                  - key: settings.xml
                    path: settings.xml
        """
        }
    }
    stages {
        stage('Setup: VERSION') {
            steps {
                container('maven') {
                    script {
                        def pomFile = readFile('pom.xml')
                        def matcher
                        matcher = pomFile =~ /<version>(.*?)<\/version>/
                        VERSION = matcher[1][1]
                        matcher = pomFile =~ /<name>(.*?)<\/name>/
                        NAME = matcher[0][1]
                    }
                }
            }
        }

        stage('Maven: BUILD') {
            when {
                expression {
                    !params.RELEASE
                }
            }
            steps {
                container('maven') {
                    sh """mvn package -s /usr/maven/settings.xml -Dmaven.wagon.http.ssl.insecure=true -DskipTests=true"""
                }
            }
        }

        stage('Maven: TEST') {
            when {
                expression {
                    !params.RELEASE
                }
            }
            steps {
                container('maven') {
                    sh 'mvn test'
                }
            }
        }

        stage('Artifactory: UPLOAD') {
            when {
                expression {
                    !params.RELEASE && params.ARTIFACTORY
                }
            }
            steps {
                container('maven') {
                    sh """mvn deploy:deploy-file -s /usr/maven/settings.xml -Durl=${ARTIFACTORY_URL}/${ARTIFACTORY_REPO_NAME} -Dpackaging=jar -DrepositoryId=${ARTIFACTORY_REPO_NAME} -Dfile=target/${NAME}.jar -Dmaven.wagon.http.ssl.insecure=true"""
                }
            }
        }
        stage('Image: BUILD & PUSH') {
            when {
                expression {
                    !params.RELEASE
                }
            }
            steps {
                container('kaniko') {
                    script {
                        sh """sed -i 's/<NAME>/${NAME}/' Dockerfile"""
                        sh """
                               /kaniko/executor --dockerfile `pwd`/Dockerfile \
                               --context `pwd` \
                               --destination=${HARBOR_URL}/${HARBOR_PREFIX}/${NAME}:${VERSION} \
                               --skip-tls-verify
                        """
                    }
                }
            }
        }

        stage('Icon: SET') {
            when {
                expression {
                    !params.RELEASE
                }
            }
            steps {
                script {
                    addShortText(text: "Version: ${VERSION}")
                }
            }
        }

        stage('K8s: DEPLOY') {
            when {
                expression {
                    !params.RELEASE
                }
            }
            steps {
                script {
                    container('kubectl') {
                        withCredentials([file(credentialsId: 'kubeConfig', variable: 'KUBECONFIG')]) {
                            sh """sed -i 's/<TAG>/${VERSION}/' ${NAME}.yaml"""
                            sh """sed -i 's/<NAME>/${NAME}/' ${NAME}.yaml"""
                            if(!params.INIT_DEPLOYMENT) {
                                sh """kubectl delete -f ${NAME}.yaml"""
                            }
                            sh """kubectl apply -f ${NAME}.yaml"""
                        }
                    }
                }
            }
        }
        stage('Setup: RELEASE') {
            when {
                expression {
                    params.RELEASE
                }
            }
            steps {
                container('maven') {
                    script {
                        withCredentials([sshUserPrivateKey(credentialsId: 'github', keyFileVariable: 'keyfile')]) {
                            sh """echo ${params.VERSION} | mvn gitflow:release"""
                            VERSION = params.VERSION
                        }
                    }
                }
            }
        }

        stage('Git: PUSH') {
            when {
                expression {
                    params.RELEASE
                }
            }
            steps {
                withCredentials([sshUserPrivateKey(credentialsId: 'github', keyFileVariable: 'keyfile')]) {
                    sh """git -c core.sshCommand="ssh -i \${keyfile} -o StrictHostKeyChecking=no" push --tags"""
                    sh """git -c core.sshCommand="ssh -i \${keyfile} -o StrictHostKeyChecking=no" push origin develop"""
                    sh """git -c core.sshCommand="ssh -i \${keyfile} -o StrictHostKeyChecking=no" push origin master"""
                }
            }
        }
    }
}
