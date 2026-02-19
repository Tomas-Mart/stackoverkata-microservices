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
import {usersService} from '../services/users';
import {format} from 'date-fns';
import {ru} from 'date-fns/locale';

export function ProfilePage() {
    const {id} = useParams<{ id: string }>();

    const {data: user, isLoading} = useQuery(
        ['user', id],
        () => usersService.getUser(id!)
    );

    if (isLoading || !user) {
        return <Typography>Загрузка...</Typography>;
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
                                    {format(new Date(user.registrationDate), 'dd MMMM yyyy', {locale: ru})}
                                </Typography>
                            </Grid>
                            <Grid item xs={4}>
                                <Typography variant="body2" color="text.secondary">
                                    Последний визит
                                </Typography>
                                <Typography variant="body1">
                                    {format(new Date(user.lastVisit), 'dd MMMM yyyy', {locale: ru})}
                                </Typography>
                            </Grid>
                        </Grid>
                    </Grid>
                </Grid>
            </Paper>
        </Box>
    );
}