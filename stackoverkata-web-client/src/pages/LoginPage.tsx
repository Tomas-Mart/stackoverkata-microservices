import React, {useState} from 'react';
import {
    Box,
    Paper,
    TextField,
    Button,
    Typography,
    Link,
    Alert,
} from '@mui/material';
import {useNavigate} from 'react-router-dom';
import {useMutation} from 'react-query';
import {authService} from '../services/auth';

export function LoginPage() {
    const navigate = useNavigate();
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const [error, setError] = useState('');

    const mutation = useMutation(
        () => authService.login({email, password}),
        {
            onSuccess: () => {
                navigate('/');
            },
            onError: (err: any) => {
                setError(err.response?.data?.message || 'Ошибка входа');
            },
        }
    );

    // @ts-ignore
    const handleEmailChange = (event: React.ChangeEvent<HTMLInputElement>) => {
        setEmail(event.target.value);
    };

    // @ts-ignore
    const handlePasswordChange = (event: React.ChangeEvent<HTMLInputElement>) => {
        setPassword(event.target.value);
    };

    // @ts-ignore
    const handleSubmit = (event: React.FormEvent<HTMLFormElement>) => {
        event.preventDefault();
        setError('');
        mutation.mutate();
    };

    return (
        <Box sx={{maxWidth: 400, mx: 'auto', mt: 8}}>
            <Paper sx={{p: 4}}>
                <Typography variant="h5" gutterBottom align="center">
                    Вход в StackOverKata
                </Typography>

                {error && (
                    <Alert severity="error" sx={{mb: 2}}>
                        {error}
                    </Alert>
                )}

                <form onSubmit={handleSubmit}>
                    <TextField
                        fullWidth
                        label="Email"
                        type="email"
                        value={email}
                        onChange={handleEmailChange}
                        margin="normal"
                        required
                    />
                    <TextField
                        fullWidth
                        label="Пароль"
                        type="password"
                        value={password}
                        onChange={handlePasswordChange}
                        margin="normal"
                        required
                    />
                    <Button
                        fullWidth
                        type="submit"
                        variant="contained"
                        size="large"
                        sx={{mt: 3, mb: 2}}
                        disabled={mutation.isLoading}
                    >
                        {mutation.isLoading ? 'Вход...' : 'Войти'}
                    </Button>
                </form>

                <Box sx={{textAlign: 'center'}}>
                    <Link
                        component="button"
                        variant="body2"
                        onClick={() => navigate('/register')}
                    >
                        Нет аккаунта? Зарегистрируйтесь
                    </Link>
                </Box>
            </Paper>
        </Box>
    );
}