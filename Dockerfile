
# Java 21 and Maven
FROM maven:3.9.6-eclipse-temurin-21

WORKDIR /app

# Install JavaFX, X11 libraries and multilingual fonts
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
    && fc-cache -f \
    && rm -rf /var/lib/apt/lists/*

# Copy Maven configuration and Java source files
COPY pom.xml .
COPY src ./src

# Build the application
RUN mvn clean package -DskipTests

# Configure JavaFX display
ENV DISPLAY=host.docker.internal:0.0
ENV JAVA_TOOL_OPTIONS="-Dprism.order=sw"

# Start JavaFX application
CMD ["mvn", "javafx:run"]
