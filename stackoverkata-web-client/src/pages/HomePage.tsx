import {Box, Typography, Button, Paper, Grid, Chip, Container, Stack} from '@mui/material';
import {useNavigate} from 'react-router-dom';
import {useQuery} from 'react-query';
import {questionsService} from '../services/questions';
import {QuestionList} from '../components/questions/QuestionList';

export function HomePage() {
    const navigate = useNavigate();
    const {data: questions, isLoading} = useQuery(
        'questions',
        () => questionsService.getQuestions(1, 10)
    );

    return (
        <Container maxWidth="lg" sx={{py: 4}}>
            {/* Герой-секция */}
            <Paper
                elevation={3}
                sx={{
                    p: 4,
                    mb: 4,
                    background: 'linear-gradient(135deg, #f48024 0%, #f9a84d 100%)',
                    color: 'white',
                    borderRadius: 2,
                    textAlign: 'center'
                }}
            >
                <Typography variant="h3" gutterBottom fontWeight="bold">
                    StackOverKata
                </Typography>
                <Typography variant="h6" sx={{mb: 3, opacity: 0.95}}>
                    Сообщество разработчиков, готовых помочь друг другу
                </Typography>
                <Button
                    variant="contained"
                    size="large"
                    onClick={() => navigate('/ask')}
                    sx={{
                        bgcolor: 'white',
                        color: '#f48024',
                        '&:hover': {
                            bgcolor: 'rgba(255,255,255,0.9)',
                        }
                    }}
                >
                    Задать вопрос
                </Button>
            </Paper>

            <Grid container spacing={4}>
                {/* Основной контент - вопросы */}
                <Grid item xs={12} md={8}>
                    <Paper sx={{p: 3}}>
                        <Typography variant="h5" gutterBottom fontWeight="medium">
                            Последние вопросы
                        </Typography>
                        <QuestionList questions={questions?.items || []} loading={isLoading}/>
                    </Paper>
                </Grid>

                {/* Боковая панель */}
                <Grid item xs={12} md={4}>
                    <Stack spacing={3}>
                        {/* Карточка с тегами */}
                        <Paper sx={{p: 3}}>
                            <Typography variant="h6" gutterBottom fontWeight="medium">
                                Популярные теги
                            </Typography>
                            <Box sx={{display: 'flex', flexWrap: 'wrap', gap: 1}}>
                                {['react', 'javascript', 'typescript', 'node.js', 'docker', 'java'].map((tag) => (
                                    <Chip
                                        key={tag}
                                        label={tag}
                                        size="small"
                                        onClick={() => navigate(`/tags/${tag}`)}
                                        sx={{
                                            bgcolor: '#e1ecf4',
                                            color: '#39739d',
                                            '&:hover': {
                                                bgcolor: '#d0e2f0',
                                                cursor: 'pointer'
                                            }
                                        }}
                                    />
                                ))}
                            </Box>
                        </Paper>

                        {/* Карточка с подпиской */}
                        <Paper sx={{p: 3, bgcolor: '#f8f9f9'}}>
                            <Typography variant="h6" gutterBottom fontWeight="medium">
                                Подпишитесь на рассылку
                            </Typography>
                            <Typography variant="body2" color="text.secondary" sx={{mb: 2}}>
                                Получайте еженедельные дайджесты лучших вопросов
                            </Typography>
                            <Button
                                variant="outlined"
                                fullWidth
                                sx={{
                                    borderColor: '#f48024',
                                    color: '#f48024',
                                    '&:hover': {
                                        borderColor: '#d96b0f',
                                        bgcolor: 'rgba(244, 128, 36, 0.04)'
                                    }
                                }}
                            >
                                Подписаться
                            </Button>
                        </Paper>
                    </Stack>
                </Grid>
            </Grid>

            {/* Футер */}
            <Box
                component="footer"
                sx={{
                    mt: 6,
                    pt: 3,
                    borderTop: '1px solid',
                    borderColor: 'divider',
                    textAlign: 'center',
                    color: 'text.secondary'
                }}
            >
                <Typography variant="body2">
                    © 2026 StackOverKata. Все права защищены.
                </Typography>
            </Box>
        </Container>
    );
}