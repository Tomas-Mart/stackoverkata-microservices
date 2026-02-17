import { serviceRegistry } from '../service-registry/ServiceRegistry';

class LoadBalancer {
    private strategy: 'ROUND_ROBIN' | 'RANDOM' = 'ROUND_ROBIN';
    private counters: Map<string, number> = new Map();

    setStrategy(strategy: 'ROUND_ROBIN' | 'RANDOM') {
        this.strategy = strategy;
    }

    async getServiceInstance(serviceName: string) {
        const services = serviceRegistry.getAvailableServices()
            .filter(s => s.name === serviceName);

        if (services.length === 0) {
            throw new Error(`Service ${serviceName} not available`);
        }

        switch (this.strategy) {
            case 'ROUND_ROBIN':
                return this.roundRobin(services, serviceName);
            case 'RANDOM':
                return this.random(services);
            default:
                return services[0];
        }
    }

    private roundRobin(services: Microservice[], serviceName: string) {
        const count = this.counters.get(serviceName) || 0;
        const index = count % services.length;
        this.counters.set(serviceName, count + 1);
        return services[index];
    }

    private random(services: Microservice[]) {
        const index = Math.floor(Math.random() * services.length);
        return services[index];
    }
}

export const loadBalancer = new LoadBalancer();