
FROM maven:3.9.6-eclipse-temurin-21

WORKDIR /app

# Install JavaFX, X11 libraries, and Japanese fonts
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

# Copy Maven configuration
COPY pom.xml .

# Copy Java source files
COPY src ./src

# Build application
RUN mvn clean package -DskipTests

# Configure XQuartz display on macOS
ENV DISPLAY=host.docker.internal:0.0

# Start JavaFX application
CMD ["mvn", "javafx:run"]
