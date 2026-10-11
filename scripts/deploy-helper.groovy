def deployToEnvironment(String envName, String targetPath) {
    echo "=================================================="
    echo "Starting Deployment to: ${envName}"
    echo "Target Directory: ${targetPath}"
    echo "=================================================="
    
    // Simulating deployment actions on the machine
    sh "mkdir -p ${targetPath}"
    sh "cp target/crud-app-1.0.0.jar ${targetPath}/app.jar"
    
    echo "Artifact successfully deployed to ${targetPath}/app.jar"
    echo "Deployment to ${envName} completed successfully."
}

return this