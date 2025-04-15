#!/bin/bash

# get the token
echo "Reading sonar token"
source sonar.env

# Step 2: Check if SONAR_TOKEN is set
if [ -z "$SONAR_TOKEN" ]; then
  echo "Error: SONAR_TOKEN is not set. Exiting..."
  exit 1
fi
echo "Sonar token is: $SONAR_TOKEN"

# Step 3: Actual SonarQube analysis
echo "Running SonarQube analysis"

# actual sonarqube analyzis
mvn clean verify sonar:sonar \
  -Dsonar.projectKey=hslu-swat \
  -Dsonar.host.url=http://localhost:9001 \
  -Dsonar.login=$SONAR_TOKEN
