import {Outlet} from 'react-router-dom';
import {Box, Container} from '@mui/material';
import {Header} from './Header';
import {Sidebar} from './Sidebar';

export function Layout() {
    return (
        <Box sx={{display: 'flex', minHeight: '100vh'}}>
            <Header/>
            <Box sx={{display: 'flex', width: '100%', pt: 8}}>
                <Sidebar/>
                <Container maxWidth="lg" sx={{py: 3, flexGrow: 1}}>
                    <Outlet/>
                </Container>
            </Box>
        </Box>
    );
}