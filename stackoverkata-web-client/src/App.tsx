import {useEffect} from 'react';
import {QueryClient, QueryClientProvider} from 'react-query';
import {BrowserRouter} from 'react-router-dom';
import {ThemeProvider, CssBaseline} from '@mui/material';
import {theme} from './styles/theme';
import {AppRoutes} from './routes';
import {authService} from './services/auth';

const queryClient = new QueryClient({
    defaultOptions: {
        queries: {
            refetchOnWindowFocus: false,
            retry: 1,
        },
    },
});

function App() {
    useEffect(() => {
        // Проверка аутентификации при загрузке
        const token = localStorage.getItem('token');
        if (token) {
            authService.getCurrentUser().catch(() => {
                localStorage.removeItem('token');
            });
        }
    }, []);

    return (
        <QueryClientProvider client={queryClient}>
            <ThemeProvider theme={theme}>
                <CssBaseline/>
                <BrowserRouter>
                    <AppRoutes/>
                </BrowserRouter>
            </ThemeProvider>
        </QueryClientProvider>
    );
}

export default App;