import { serviceRegistry } from '../service-registry/ServiceRegistry';

export class HealthService {
    static async checkAllServices() {
        const results = [];
        for (const service of serviceRegistry.getAvailableServices()) {
            const start = Date.now();
            try {
                const response = await fetch(service.healthCheckUrl);
                results.push({
                    name: service.name,
                    status: response.ok ? 'HEALTHY' : 'UNHEALTHY',
                    responseTime: Date.now() - start,
                    timestamp: new Date().toISOString()
                });
            } catch (error) {
                results.push({
                    name: service.name,
                    status: 'DOWN',
                    responseTime: -1,
                    timestamp: new Date().toISOString(),
                    error: error.message
                });
            }
        }
        return results;
    }

    static async getServiceHealth(name: string) {
        const service = serviceRegistry.getService(name);
        if (!service) return { status: 'NOT_FOUND' };

        const start = Date.now();
        try {
            const response = await fetch(service.healthCheckUrl);
            return {
                status: response.ok ? 'HEALTHY' : 'UNHEALTHY',
                responseTime: Date.now() - start,
                timestamp: new Date().toISOString()
            };
        } catch (error) {
            return {
                status: 'DOWN',
                responseTime: -1,
                timestamp: new Date().toISOString(),
                error: error.message
            };
        }
    }
}