pipeline {
  agent any

  environment {
    git_commit_id = getGitLastCommitId()

    envt  = "HITPA-QA-"
    cname = "HITPA-QA-Grievance-Service"
    thost = "hitpa_qa"
    rname = "hitpa_grievance_service"

    AWS_REGION = "ap-south-1"
    ECR_URL    = "487148650694.dkr.ecr.ap-south-1.amazonaws.com"

    // TEMP ansible location (outside workspace)
    ANSIBLE_TMP = "/tmp/ansible_playbook_${BUILD_ID}"
  }

  stages {

    stage('Checkout Application Repo') {
      steps {
        checkout scm
      }
    }

    stage('Checkout Ansible Repo (TEMP)') {
      steps {
        sh '''
          rm -rf ${ANSIBLE_TMP}
          git clone -b hitpa_qa git@github.com:wisteli/ansible_playbook.git ${ANSIBLE_TMP}
        '''
      }
    }

    stage('Copy env file (TEMP usage only)') {
      steps {
        sh '''
          if [ -f ${ANSIBLE_TMP}/grievance.env ]; then
            cp ${ANSIBLE_TMP}/grievance.env .env
          else
            echo "ERROR: grievance.env not found"
            exit 1
          fi
        '''
      }
    }

    stage('Build Docker Image') {
      steps {
        sh '''
          docker build . -t ${rname}:${envt}${git_commit_id}

          docker tag ${rname}:${envt}${git_commit_id} \
            ${ECR_URL}/${rname}:${envt}${git_commit_id}
        '''
      }
    }

    stage('Push Docker Image to AWS ECR') {
      steps {
        sh '''
          aws ecr get-login-password --region ${AWS_REGION} | \
          docker login --username AWS --password-stdin ${ECR_URL}

          docker push ${ECR_URL}/${rname}:${envt}${git_commit_id}
        '''
      }
    }

    stage('Deploy using Ansible') {
      steps {
        ansiblePlaybook(
          credentialsId: 'gcp',
          disableHostKeyChecking: true,
          inventory: "${ANSIBLE_TMP}/Inventory.txt",
          playbook: "${ANSIBLE_TMP}/deploy_grievance_service.yml",
          installation: 'ansible',
          extras: "-e thost=${thost} " +
                  "-e docker_image=${ECR_URL}/${rname}:${envt}${git_commit_id} " +
                  "-e container_name=${cname} " +
                  "-e ansible_python_interpreter=/usr/bin/python3"
        )
      }
    }
  }

  post {
    always {
      // Remove ONLY sensitive artifacts
      sh '''
        rm -rf ${ANSIBLE_TMP}
        rm -f .env
      '''
    }

    success {
      echo "Pipeline completed successfully."
    }

    failure {
      echo "Pipeline failed. Check logs."
    }
  }
}

/* ---------------- HELPER FUNCTION ---------------- */

def getGitLastCommitId() {
  return sh(
    script: "git rev-parse --short HEAD",
    returnStdout: true
  ).trim()
}
