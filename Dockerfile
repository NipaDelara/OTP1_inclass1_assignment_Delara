FROM maven:3.9.6-eclipse-temurin-21

WORKDIR /app

# Install libraries required by JavaFX and X11
RUN apt-get update && apt-get install -y \
    libgtk-3-0 \
    libx11-6 \
    libxext6 \
    libxi6 \
    libxrender1 \
    libxtst6 \
    libgl1 \
    libasound2 \
    && rm -rf /var/lib/apt/lists/*

COPY pom.xml .

COPY src ./src

# Build application
RUN mvn clean package

# X11 display used by Xming
ENV DISPLAY=host.docker.internal:0.0

# Start JavaFX application
CMD ["mvn", "javafx:run"]