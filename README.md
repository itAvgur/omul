# Omul Getting Started

### postgres

docker run --rm --name postgres --network omul -p 5432:5432 -v ~/pg/omul:/var/lib/postgresql/data -v ~/data:/data -e
POSTGRES_USER=root -e POSTGRES_PASSWORD=root -e POSTGRES_DB=omul -d postgres:15.6

## ingress

helm repo add ingress-nginx https://kubernetes.github.io/ingress-nginx/

helm repo update

helm install nginx ingress-nginx/ingress-nginx --namespace ingress-nginx --create-namespace -f helm/nginx-ingress.yaml

kubectl create namespace omul

## omul - mock, test only local services

helm install omul-mock helm/ -f helm/values-mock.yaml

newman run ./postman/mock_no_jwt.collection.json -e ./postman/localhost.env.json

## omul, no auth, direct access to rest api

helm install omul helm/ -f helm/values.yaml

newman run postman/e2e_no_jwt.collection.json -e postman/localhost.env.json

## omul, with auth, access via nginx api-gateway

helm install omul helm/ -f helm/values.yaml

newman run postman/e2e_jwt.collection.json -e postman/k8s.env.json --env-var jwt={token}

### sign user (get jwt)

curl --location 'http://localhost:8105/v1/auth/register' \
--header 'Content-Type: application/json' \
--data-raw '{
"login": "it.avgur@test.com",
"userId": "0",
"role": "CUSTOMER",
"password": "pass"
}'

### auth user (get jwt)

curl --location 'http://localhost:8105/v1/auth/login/pass' \
--header 'Content-Type: application/json' \
--data-raw '{
"login": "it.avgur@test.com",
"password": "pass"
}'

### sign user (get jwt)

curl --location 'http://host1869082-1.hostland.pro/auth/register' \
--header 'Content-Type: application/json' \
--data-raw '{
"login": "it.avgur@test.com",
"userId": "0",
"role": "CUSTOMER",
"password": "pass"
}'

### auth user (get jwt)

curl --location 'http://host1869082-1.hostland.pro/auth/login/pass' \
--header 'Content-Type: application/json' \
--data-raw '{
"login": "it.avgur@test.com",
"password": "pass"
}'
