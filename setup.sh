#!/bin/bash
# ============================================================
# QUICK SETUP SCRIPT — Smart Clinic Management System
# ============================================================
# This script creates the entire project structure automatically.
# Just run it and it does everything for you!
#
# HOW TO USE:
# 1. Save this file as setup.sh
# 2. Run: chmod +x setup.sh
# 3. Run: ./setup.sh
# ============================================================

echo "🏥 Setting up Smart Clinic Management System..."
echo ""

# Create project structure
echo "📁 Creating folder structure..."
mkdir -p app/src/main/java/com/project/back_end/{models,repository,services,controller,mvc,config,security,dto}
mkdir -p app/src/main/resources/{static/{assets/{css,images/{logo,edit,defineRole,addPrescriptionIcon}},js/{components,config,services},pages},templates/{admin,doctor}}
mkdir -p app/src/test/java
mkdir -p docs
mkdir -p .github/workflows

echo "✅ Folder structure created!"
echo ""

# Check prerequisites
echo "🔍 Checking prerequisites..."

# Check Java
if command -v java &> /dev/null; then
    echo "✅ Java: $(java -version 2>&1 | head -n 1)"
else
    echo "❌ Java not found! Install from https://adoptium.net/"
    exit 1
fi

# Check Maven
if command -v mvn &> /dev/null; then
    echo "✅ Maven: $(mvn -version 2>&1 | head -n 1)"
else
    echo "❌ Maven not found! Install from https://maven.apache.org/download.cgi"
    exit 1
fi

# Check MySQL
if command -v mysql &> /dev/null; then
    echo "✅ MySQL: $(mysql --version)"
else
    echo "❌ MySQL not found! Install from https://dev.mysql.com/downloads/mysql/"
    exit 1
fi

# Check MongoDB
if command -v mongod &> /dev/null; then
    echo "✅ MongoDB: $(mongod --version | head -n 1)"
else
    echo "❌ MongoDB not found! Install from https://www.mongodb.com/try/download/community"
    exit 1
fi

# Check Git
if command -v git &> /dev/null; then
    echo "✅ Git: $(git --version)"
else
    echo "❌ Git not found! Install from https://git-scm.com/"
    exit 1
fi

echo ""
echo "✅ All prerequisites found!"
echo ""

# Initialize Git
echo "📦 Initializing Git repository..."
git init
echo "✅ Git initialized!"
echo ""

echo "🎉 Setup complete!"
echo ""
echo "NEXT STEPS:"
echo "1. Copy all project files into the created folders"
echo "2. Set up MySQL: mysql -u root -p < docs/sample-data.sql"
echo "3. Set up MongoDB: mongosh < docs/mongodb-prescriptions.js"
echo "4. Run the app: cd app && mvn spring-boot:run"
echo "5. Open: http://localhost:8080"
echo ""
echo "See RUN-GUIDE.md for detailed instructions!"
