#!/bin/bash

# Script para subir CPT Mobile a Play Store

echo "🚀 Subiendo CPT Mobile a Play Store (Internal)..."

# Configurar Java
export JAVA_HOME=/Applications/Android\ Studio.app/Contents/jbr/Contents/Home
export PATH=$JAVA_HOME/bin:$PATH

# Generar AAB
echo "📦 Generando App Bundle..."
./gradlew bundleRelease

if [ $? -ne 0 ]; then
    echo "❌ Error generando AAB"
    exit 1
fi

# Subir a Play Store
echo "⬆️ Subiendo a Play Store..."
fastlane deploy_internal

if [ $? -eq 0 ]; then
    echo "✅ Subida exitosa! Revisá en: https://play.google.com/console"
else
    echo "❌ Error subiendo a Play Store"
    exit 1
fi
