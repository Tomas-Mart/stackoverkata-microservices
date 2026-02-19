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

export function RegisterPage() {
    const navigate = useNavigate();
    const [username, setUsername] = useState('');
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const [confirmPassword, setConfirmPassword] = useState('');
    const [error, setError] = useState('');

    const mutation = useMutation(
        () => authService.register({username, email, password}),
        {
            onSuccess: () => {
                navigate('/');
            },
            onError: (err: any) => {
                setError(err.response?.data?.message || 'Ошибка регистрации');
            },
        }
    );

    const handleSubmit = (e: React.FormEvent) => {
        e.preventDefault();
        setError('');

        if (password !== confirmPassword) {
            setError('Пароли не совпадают');
            return;
        }

        mutation.mutate();
    };

    return (
        <Box sx={{maxWidth: 400, mx: 'auto', mt: 8}}>
            <Paper sx={{p: 4}}>
                <Typography variant="h5" gutterBottom align="center">
                    Регистрация в StackOverKata
                </Typography>

                {error && (
                    <Alert severity="error" sx={{mb: 2}}>
                        {error}
                    </Alert>
                )}

                <form onSubmit={handleSubmit}>
                    <TextField
                        fullWidth
                        label="Имя пользователя"
                        value={username}
                        onChange={(e) => setUsername(e.target.value)}
                        margin="normal"
                        required
                    />
                    <TextField
                        fullWidth
                        label="Email"
                        type="email"
                        value={email}
                        onChange={(e) => setEmail(e.target.value)}
                        margin="normal"
                        required
                    />
                    <TextField
                        fullWidth
                        label="Пароль"
                        type="password"
                        value={password}
                        onChange={(e) => setPassword(e.target.value)}
                        margin="normal"
                        required
                    />
                    <TextField
                        fullWidth
                        label="Подтверждение пароля"
                        type="password"
                        value={confirmPassword}
                        onChange={(e) => setConfirmPassword(e.target.value)}
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
                        {mutation.isLoading ? 'Регистрация...' : 'Зарегистрироваться'}
                    </Button>
                </form>

                <Box sx={{textAlign: 'center'}}>
                    <Link
                        component="button"
                        variant="body2"
                        onClick={() => navigate('/login')}
                    >
                        Уже есть аккаунт? Войдите
                    </Link>
                </Box>
            </Paper>
        </Box>
    );
}