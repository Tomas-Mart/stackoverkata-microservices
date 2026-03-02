import {
    Box,
    Paper,
    Typography,
    Avatar,
    Grid,
    Chip,
    Divider,
} from '@mui/material';
import {useParams} from 'react-router-dom';
import {useQuery} from 'react-query';
import {usersService} from '../services/users'; // Проверьте, что файл users.ts существует
import {format} from 'date-fns';
import {ru} from 'date-fns/locale';

// Интерфейс для пользователя (если не импортируется из usersService)
interface User {
    id: number;
    fullName?: string;
    email: string;
    linkPhoto?: string;
    city?: string;
    reputation: number;
    registrationDate: string;
    lastVisit: string;
}

export function ProfilePage() {
    const {id} = useParams;

    const {data: user, isLoading} = useQuery<User>(
        ['user', id],
        () => usersService.getUser(id!),
        {
            enabled: !!id, // Запрос только если есть id
        }
    );

    if (isLoading) {
        return (
            <Box sx={{display: 'flex', justifyContent: 'center', mt: 4}}>
                <Typography>Загрузка...</Typography>
            </Box>
        );
    }

    if (!user) {
        return (
            <Box sx={{display: 'flex', justifyContent: 'center', mt: 4}}>
                <Typography>Пользователь не найден</Typography>
            </Box>
        );
    }

    return (
        <Box>
            <Paper sx={{p: 4, mb: 3}}>
                <Grid container spacing={3}>
                    <Grid item xs={12} md={3} sx={{textAlign: 'center'}}>
                        <Avatar
                            src={user.linkPhoto}
                            sx={{width: 150, height: 150, mx: 'auto', mb: 2}}
                        >
                            {user.fullName?.[0] || 'U'}
                        </Avatar>
                        <Typography variant="h5" gutterBottom>
                            {user.fullName || 'Пользователь'}
                        </Typography>
                        <Typography variant="body2" color="text.secondary" gutterBottom>
                            {user.email}
                        </Typography>
                        <Chip
                            label={`Репутация: ${user.reputation}`}
                            color="primary"
                            sx={{mt: 1}}
                        />
                    </Grid>

                    <Grid item xs={12} md={9}>
                        <Typography variant="h6" gutterBottom>
                            О пользователе
                        </Typography>
                        <Divider sx={{mb: 2}}/>

                        <Grid container spacing={2}>
                            <Grid item xs={4}>
                                <Typography variant="body2" color="text.secondary">
                                    Город
                                </Typography>
                                <Typography variant="body1">
                                    {user.city || 'Не указан'}
                                </Typography>
                            </Grid>
                            <Grid item xs={4}>
                                <Typography variant="body2" color="text.secondary">
                                    Дата регистрации
                                </Typography>
                                <Typography variant="body1">
                                    {user.registrationDate ?
                                        format(new Date(user.registrationDate), 'dd MMMM yyyy', {locale: ru}) :
                                        'Не указана'}
                                </Typography>
                            </Grid>
                            <Grid item xs={4}>
                                <Typography variant="body2" color="text.secondary">
                                    Последний визит
                                </Typography>
                                <Typography variant="body1">
                                    {user.lastVisit ?
                                        format(new Date(user.lastVisit), 'dd MMMM yyyy', {locale: ru}) :
                                        'Не указан'}
                                </Typography>
                            </Grid>
                        </Grid>
                    </Grid>
                </Grid>
            </Paper>
        </Box>
    );
}