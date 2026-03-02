import {useState} from 'react';
import {
    Box,
    Typography,
    Paper,
    Avatar,
    TextField,
    InputAdornment,
    Grid,
} from '@mui/material';
import SearchIcon from '@mui/icons-material/Search';
import {useQuery} from 'react-query';
import {Link} from 'react-router-dom';
import {usersService, UsersResponse} from '../services/users';
import {User} from '../types/user';

export function UsersPage() {
    const [search, setSearch] = useState('');

    const {data, isLoading} = useQuery<UsersResponse>(
        ['users', search],
        () => usersService.getUsers(search)
    );

    const handleSearchChange = (event: any) => {
        setSearch(event.target.value);
    };

    if (isLoading) {
        return (
            <Box sx={{display: 'flex', justifyContent: 'center', mt: 4}}>
                <Typography>Загрузка пользователей...</Typography>
            </Box>
        );
    }

    const users = data?.items || [];

    return (
        <Box>
            <Typography variant="h4" gutterBottom>
                Пользователи
            </Typography>

            <TextField
                fullWidth
                placeholder="Поиск пользователей..."
                value={search}
                onChange={handleSearchChange}
                sx={{mb: 3}}
                InputProps={{
                    startAdornment: (
                        <InputAdornment position="start">
                            <SearchIcon/>
                        </InputAdornment>
                    ),
                }}
            />

            {users.length === 0 ? (
                <Typography variant="body1" color="text.secondary" sx={{textAlign: 'center', mt: 4}}>
                    Пользователи не найдены
                </Typography>
            ) : (
                <Grid container spacing={2}>
                    {users.map((user: User) => (
                        <Grid item xs={12} sm={6} md={4} key={user.id}>
                            <Paper
                                sx={{
                                    p: 2,
                                    display: 'flex',
                                    alignItems: 'center',
                                    gap: 2,
                                    '&:hover': {
                                        boxShadow: 3,
                                    },
                                }}
                            >
                                <Avatar
                                    src={user.linkPhoto}
                                    sx={{width: 56, height: 56}}
                                >
                                    {user.fullName?.[0] || 'U'}
                                </Avatar>
                                <Box>
                                    <Link
                                        to={`/users/${user.id}`}
                                        style={{textDecoration: 'none'}}
                                    >
                                        <Typography variant="h6" color="primary">
                                            {user.fullName || 'Пользователь'}
                                        </Typography>
                                    </Link>
                                    <Typography variant="body2" color="text.secondary">
                                        Репутация: {user.reputation}
                                    </Typography>
                                    <Typography variant="caption" color="text.secondary">
                                        Город: {user.city || 'Не указан'}
                                    </Typography>
                                </Box>
                            </Paper>
                        </Grid>
                    ))}
                </Grid>
            )}
        </Box>
    );
}