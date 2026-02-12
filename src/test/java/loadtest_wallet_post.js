import http from 'k6/http';
import { check } from 'k6';

export const options = {
    scenarios: {
        steady_1000_rps: {
            executor: 'constant-arrival-rate',
rate: 1000,
    timeUnit: '1s',
duration: '1m',
preAllocatedVUs: 200,
    maxVUs: 3000,
},
},
thresholds: {
    http_req_failed: ['rate<0.01'],
},
};

export default function () {
    const url = 'http://localhost:8080/api/v1/wallet'

        const payload = JSON.stringify({
            walletId: '550e8400-e29b-41d4-a716-446655440009',
    operationType: 'DEPOSIT',
    amount: 100
});

    const res = http.post(url, payload, {
        headers: { 'Content-Type' : 'application/json'}
    timeout: '30s',
});
    )
}