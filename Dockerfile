#
# Copyright 2025 Hochschule Luzern Informatik.
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
#      http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.
#
FROM amazoncorretto:21.0.6-alpine
LABEL maintainer="Roland Gisler <roland.gisler@hslu.ch>, HSLU Informatik"
LABEL version="1.0.0"
LABEL description="Simple Java project template with CI/CD."
RUN adduser -D worker
USER worker
WORKDIR /opt/demo
CMD ["java","-jar","/opt/demo/oop_maven_template.jar"]
COPY target/oop_maven_template.jar /opt/demo/oop_maven_template.jar
