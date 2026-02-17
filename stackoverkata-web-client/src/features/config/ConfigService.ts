import axios from 'axios';
import { loadBalancer } from '../load-balancing/LoadBalancer';

class ConfigService {
    private cache: Map<string, any> = new Map();
    private lastUpdated: Date | null = null;

    async getConfig(serviceName: string, refresh = false) {
        if (!refresh && this.cache.has(serviceName)) {
            return this.cache.get(serviceName);
        }

        try {
            const configServer = await loadBalancer.getServiceInstance('config-service');
            const response = await axios.get(`${configServer.url}/config/${serviceName}`);
            this.cache.set(serviceName, response.data);
            this.lastUpdated = new Date();
            return response.data;
        } catch (error) {
            console.error('Failed to fetch config:', error);
            throw error;
        }
    }

    async refreshAllConfigs() {
        const services = ['auth-service', 'profile-service', 'resource-service', 'email-service'];
        for (const service of services) {
            await this.getConfig(service, true);
        }
    }
}

export const configService = new ConfigService();