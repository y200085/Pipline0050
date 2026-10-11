git init
git add .
git commit -m "Add complete CI/CD pipeline, Dockerfile, and Java app files"
git branch -M development
git remote add origin https://github.com/y200085/Pipline0050.git
git push -u origin development

Open your Jenkins dashboard (http://localhost:8080).

Click New Item on the left-hand menu.

Enter a name (e.g., java-crud-multibranch), select Multibranch Pipeline, and click OK.

Under the Branch Sources section, click Add source and select Git.

Paste your GitHub Repository URL and supply credentials if required.

Verify that the Script Path is set to Jenkinsfile.

Click Save.