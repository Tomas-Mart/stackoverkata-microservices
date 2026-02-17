interface Microservice {
    name: string;
    url: string;
    healthCheckUrl: string;
    lastUpdated: Date;
    status: 'UP' | 'DOWN';
}

class ServiceRegistry {
    private services: Map<string, Microservice> = new Map();
    private checkInterval: number = 30000; // 30 сек

    constructor() {
        setInterval(() => this.checkServicesHealth(), this.checkInterval);
    }

    registerService(service: Omit<Microservice, 'status' | 'lastUpdated'>) {
        this.services.set(service.name, {
            ...service,
            status: 'UP',
            lastUpdated: new Date()
        });
    }

    async checkServicesHealth() {
        for (const [name, service] of this.services) {
            try {
                const response = await fetch(service.healthCheckUrl);
                service.status = response.ok ? 'UP' : 'DOWN';
            } catch {
                service.status = 'DOWN';
            }
            service.lastUpdated = new Date();
        }
    }

    getAvailableServices() {
        return Array.from(this.services.values()).filter(s => s.status === 'UP');
    }

    getService(name: string) {
        return this.services.get(name);
    }
}

export const serviceRegistry = new ServiceRegistry();