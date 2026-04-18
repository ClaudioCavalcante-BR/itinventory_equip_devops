import http from 'k6/http';

export const options = {
    vus: 100,
    duration: '90s',
};

const token = 'eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxIiwiaWF0IjoxNzc1MzUwNTUwLCJleHAiOjE3NzUzNTQxNTAsIm5hbWUiOiJQYXVsbyBIZW5yaXF1ZSBNYXJ0aW5zIiwiZW1haWwiOiJwYXVsby5tYXJ0aW5zMDUxQGVtcHJlc2EuY29tIiwiam9iVGl0bGUiOiJBZG1pbmlzdHJhZG9yIGRvIFNpc3RlbWEiLCJwcm9maWxlQ29kZSI6IkFETUlOIiwibml2ZWxBY2Vzc28iOjN9.uOS8t-2Ef5RS6eXL7bA4HQt_dNwaEnhEuBZB_ix8AiI';

export default function () {
    http.get('http://127.0.0.1:50681/api/equipments', {
        headers: {
            Authorization: `Bearer ${token}`,
        },
    });
}