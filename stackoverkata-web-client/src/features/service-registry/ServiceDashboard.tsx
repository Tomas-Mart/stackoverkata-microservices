import { useEffect, useState } from 'react';
import { HealthService } from '../health/HealthService';
import { Table, TableBody, TableCell, TableContainer, TableHead, TableRow, Paper, Chip } from '@mui/material';

export const ServiceDashboard = () => {
    const [services, setServices] = useState([]);

    useEffect(() => {
        const interval = setInterval(async () => {
            const status = await HealthService.checkAllServices();
            setServices(status);
        }, 5000);

        return () => clearInterval(interval);
    }, []);

    return (
        <TableContainer component={Paper}>
            <Table>
                <TableHead>
                    <TableRow>
                        <TableCell>Service</TableCell>
                        <TableCell align="right">Status</TableCell>
                        <TableCell align="right">Response Time</TableCell>
                        <TableCell align="right">Last Check</TableCell>
                    </TableRow>
                </TableHead>
                <TableBody>
                    {services.map((service) => (
                        <TableRow key={service.name}>
                            <TableCell>{service.name}</TableCell>
                            <TableCell align="right">
                                <Chip
                                    label={service.status}
                                    color={
                                        service.status === 'HEALTHY' ? 'success' :
                                            service.status === 'UNHEALTHY' ? 'warning' : 'error'
                                    }
                                />
                            </TableCell>
                            <TableCell align="right">
                                {service.responseTime > 0 ? `${service.responseTime}ms` : '-'}
                            </TableCell>
                            <TableCell align="right">{service.timestamp}</TableCell>
                        </TableRow>
                    ))}
                </TableBody>
            </Table>
        </TableContainer>
    );
};