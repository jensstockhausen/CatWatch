#!/bin/bash

# Install Node.js via Package Manager & Add Package Source
curl -sL https://deb.nodesource.com/setup_10.x | sudo -E bash -  # Install NodeJS v10
sudo apt-get install -y nodejs  # npm nodejs-legacy #(Installed with nodesource)

# Update Node Package Manager (NPM)
sudo npm install npm@latest -g

# Get Version info
echo "[NPM] ============"; which npm; npm -v;
echo "[NODE] ============"; which node; node -v

# Install PM2
sudo npm install -g pm2


# Setup PM2 Startup Script
# sudo pm2 startup  # To Start PM2 as root
#pm2 startup  # To start PM2 as pi / current user

# Start CNCjs (on port 8000, /w Tinyweb mount point) with PM2
#pm2 start $(which cncjs) -- --port 8000 -m /tinyweb:/home/pi/tinyweb

# Set current running apps to startup
#pm2 save

# Get list of PM2 processes
#pm2 list