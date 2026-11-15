import { useEffect } from 'react';

const SwaggerRedirect = () => {
  useEffect(() => {
    window.location.replace('http://localhost:8080/swagger-ui/index.html');
  }, []);

  return null; 
};

export default SwaggerRedirect;