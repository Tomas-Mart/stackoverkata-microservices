import React from 'react';
import {Box, List, ListItem, ListItemButton, ListItemIcon, ListItemText, Paper} from '@mui/material';
import HomeIcon from '@mui/icons-material/Home';
import QuestionAnswerIcon from '@mui/icons-material/QuestionAnswer';
import TagIcon from '@mui/icons-material/Tag';
import PeopleIcon from '@mui/icons-material/People';
import {useNavigate} from 'react-router-dom';

export function Sidebar() {
    const navigate = useNavigate();

    const menuItems = [
        {text: 'Главная', icon: <HomeIcon/>, path: '/'},
        {text: 'Вопросы', icon: <QuestionAnswerIcon/>, path: '/questions'},
        {text: 'Теги', icon: <TagIcon/>, path: '/tags'},
        {text: 'Пользователи', icon: <PeopleIcon/>, path: '/users'},
    ];

    return (
        <Paper
            sx={{
                width: 240,
                height: 'calc(100vh - 64px)',
                position: 'fixed',
                top: 64,
                left: 0,
                borderRadius: 0,
                borderRight: '1px solid',
                borderColor: 'divider',
            }}
        >
            <Box sx={{overflow: 'auto', mt: 2}}>
                <List>
                    {menuItems.map((item) => (
                        <ListItem key={item.text} disablePadding>
                            <ListItemButton onClick={() => navigate(item.path)}>
                                <ListItemIcon>{item.icon}</ListItemIcon>
                                <ListItemText primary={item.text}/>
                            </ListItemButton>
                        </ListItem>
                    ))}
                </List>
            </Box>
        </Paper>
    );
}