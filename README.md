### Preconditions
Download minikube from here: https://github.com/kubernetes/minikube/releases   
Install kubectl: https://kubernetes.io/docs/tasks/tools/install-kubectl

### Local Start-Up
#### Start Minikube
```
minikube start --driver=docker
```

#### Setup Minikube Acccount
```
kubectl create rolebinding default-sa-view --clusterrole=view --serviceaccount=default:default --namespace=default
```  

#### Setup Docker and point shell to minikube's docker-daemon
```
minikube docker-env
eval $(minikube -p minikube docker-env)
```

#### Build project
```
mvn clean install
```

#### Build and push Docker image
```
docker build -t config-demo .
```

#### Apply Configmaps
```
kubectl apply -f src/k8s/config-map.yaml
kubectl apply -f src/k8s/article-protection-config.yaml
```

#### Deploy the Pod
```
kubectl apply -f src/k8s/pod.yaml
```

#### Foreard port to access via http://127.0.0.1:8080/
```
kubectl port-forward pod/config-demo 8080:8080
```

#### Check Pod Logs
```
kubectl logs -f pod/config-demo
```

#### Shutdown Minikube
```
minikube stop
```