import { useEffect } from 'react';
import { QueryClient, QueryClientProvider } from 'react-query';
import { BrowserRouter } from 'react-router-dom';
import { CssBaseline, ThemeProvider } from '@mui/material';
import theme from './styles/theme';
import AppRoutes from './routes';
import { authIntegration } from './api/AuthIntegration';
import { ServiceStatusProvider } from './features/service-registry/ServiceStatusContext';

const queryClient = new QueryClient();

function App() {
    useEffect(() => {
        authIntegration.initialize();
    }, []);

    return (
        <QueryClientProvider client={queryClient}>
            <ThemeProvider theme={theme}>
                <CssBaseline />
                <BrowserRouter>
                    <ServiceStatusProvider>
                        <AppRoutes />
                    </ServiceStatusProvider>
                </BrowserRouter>
            </ThemeProvider>
        </QueryClientProvider>
    );
}

export default App;