FROM maven:3.9.6-eclipse-temurin-21

WORKDIR /app

# Install JavaFX GUI dependencies and VNC
RUN apt-get update && apt-get install -y \
    libgtk-3-0 \
    libx11-6 \
    libxext6 \
    libxi6 \
    libxrender1 \
    libxtst6 \
    libgl1 \
    libasound2 \
    fonts-noto-cjk \
    fonts-noto-core \
    fontconfig \
    xvfb \
    x11vnc \
    x11-utils \
    xauth \
    && fc-cache -f \
    && rm -rf /var/lib/apt/lists/*

# Copy project files
COPY pom.xml .
COPY src ./src

# Build application
RUN mvn clean package -DskipTests

# JavaFX software rendering
ENV JAVA_TOOL_OPTIONS="-Dprism.order=sw"

# Start JavaFX with virtual display
CMD ["xvfb-run", "-a", "mvn", "javafx:run"]