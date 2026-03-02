import React, {useState} from 'react';
import {
    Box,
    TextField,
    Button,
    Paper,
    Typography,
    Chip,
    Autocomplete,
} from '@mui/material';
import {useNavigate} from 'react-router-dom';
import {useMutation} from 'react-query';
import {questionsService} from '../../services/questions';

export function QuestionForm() {
    const navigate = useNavigate();
    const [title, setTitle] = useState('');
    const [body, setBody] = useState('');
    const [tags, setTags] = useState<string[]>([]);
    const [tagInput, setTagInput] = useState('');

    const mutation = useMutation(
        () => questionsService.createQuestion({title, body, tags}),
        {
            onSuccess: (data) => {
                navigate(`/questions/${data.id}`);
            },
        }
    );

    const handleAddTag = (event: React.KeyboardEvent) => {
        if (event.key === 'Enter' && tagInput.trim()) {
            event.preventDefault();
            if (!tags.includes(tagInput.trim())) {
                setTags([...tags, tagInput.trim()]);
            }
            setTagInput('');
        }
    };

    const handleDeleteTag = (tagToDelete: string) => {
        setTags(tags.filter(tag => tag !== tagToDelete));
    };

    const handleSubmit = (e: React.FormEvent) => {
        e.preventDefault();
        if (title.trim() && body.trim()) {
            mutation.mutate();
        }
    };

    return (
        <Paper sx={{p: 4, maxWidth: 800, mx: 'auto'}}>
            <Typography variant="h4" gutterBottom>
                Задать вопрос
            </Typography>

            <form onSubmit={handleSubmit}>
                <TextField
                    fullWidth
                    label="Заголовок"
                    value={title}
                    onChange={(e) => setTitle(e.target.value)}
                    margin="normal"
                    required
                    placeholder="Например: Как исправить ошибку NullPointerException в Java?"
                />

                <TextField
                    fullWidth
                    label="Описание"
                    value={body}
                    onChange={(e) => setBody(e.target.value)}
                    margin="normal"
                    required
                    multiline
                    rows={8}
                    placeholder="Опишите вашу проблему подробно..."
                />

                <Box sx={{mt: 2, mb: 1}}>
                    <Typography variant="subtitle2" gutterBottom>
                        Теги
                    </Typography>
                    <Autocomplete
                        freeSolo
                        multiple
                        options={[]}
                        value={tags}
                        inputValue={tagInput}
                        onInputChange={(_, newValue) => setTagInput(newValue)}
                        onChange={(_, newValue) => setTags(newValue as string[])}
                        renderTags={(value, getTagProps) =>
                            value.map((option, index) => (
                                <Chip
                                    label={option}
                                    {...getTagProps({index})}
                                    onDelete={() => handleDeleteTag(option)}
                                    sx={{bgcolor: '#e1ecf4', color: '#39739d'}}
                                />
                            ))
                        }
                        renderInput={(params) => (
                            <TextField
                                {...params}
                                placeholder="Введите тег и нажмите Enter"
                                onKeyDown={handleAddTag}
                            />
                        )}
                    />
                </Box>

                <Box sx={{mt: 3, display: 'flex', gap: 2}}>
                    <Button
                        type="submit"
                        variant="contained"
                        size="large"
                        disabled={!title.trim() || !body.trim() || mutation.isLoading}
                    >
                        {mutation.isLoading ? 'Отправка...' : 'Опубликовать вопрос'}
                    </Button>
                    <Button
                        variant="outlined"
                        size="large"
                        onClick={() => navigate(-1)}
                    >
                        Отмена
                    </Button>
                </Box>
            </form>
        </Paper>
    );
}