import { useEffect } from 'react';

// 1. Creas el componente
const SwaggerRedirect = () => {
  useEffect(() => {
    window.location.replace('http://localhost:8080/swagger-ui/index.html');
  }, []);

  return null; 
};

export default SwaggerRedirect;