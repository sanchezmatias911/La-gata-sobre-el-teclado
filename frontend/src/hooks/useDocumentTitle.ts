import { useEffect } from 'react';

export const useDocumentTitle = (title: string) => {
    useEffect(() => {
        document.title = `${title} - La Gata Sobre el Teclado`;
    }, [title]);
};