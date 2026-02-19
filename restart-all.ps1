# restart-all.ps1
Write-Host "========================================" -ForegroundColor Cyan
Write-Host "🔄 ПЕРЕЗАПУСК ВСЕЙ СИСТЕМЫ" -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan

# Остановка всего
Write-Host "`n⏹️ Останавливаем контейнеры..." -ForegroundColor Yellow
docker-compose down

# Пересборка и запуск
Write-Host "`n🚀 Запускаем контейнеры..." -ForegroundColor Yellow
docker-compose up -d

Write-Host "`n⏳ Ожидание 30 секунд..." -ForegroundColor Yellow
Start-Sleep -Seconds 30

# Проверка статуса
Write-Host "`n📊 Статус контейнеров:" -ForegroundColor Green
docker-compose ps

Write-Host "`n========================================" -ForegroundColor Cyan
Write-Host "✅ Система запущена!" -ForegroundColor Green
Write-Host "🌐 Eureka: http://localhost:8761" -ForegroundColor Green
Write-Host "🌐 Web Client: http://localhost:8085" -ForegroundColor Green
Write-Host "========================================" -ForegroundColor Cyan