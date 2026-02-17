import axios from 'axios';
import { serviceRegistry } from '../features/service-registry/ServiceRegistry';

class AuthIntegration {
    private token: string | null = null;

    async initialize() {
        // Регистрация сервисов при инициализации
        serviceRegistry.registerService({
            name: 'auth-service',
            url: 'http://localhost:8080',
            healthCheckUrl: 'http://localhost:8080/actuator/health'
        });

        // Периодическая проверка здоровья
        setInterval(() => {
            serviceRegistry.checkServicesHealth();
        }, 30000);
    }

    async login(credentials: { email: string; password: string }) {
        const authService = await serviceRegistry.getService('auth-service');
        const response = await axios.post(`${authService.url}/api/auth/login`, credentials);
        this.token = response.data.token;
        return response.data;
    }

    async getToken() {
        return this.token;
    }
}

export const authIntegration = new AuthIntegration();