### kubectl

Increase kubectl logging output with `-v=10`, for example:

```
kubectl get pods -n test-user -v=10
```

Doing this will give you more information on what happened from the `kubectl` client-side of things.

For example the following line tells us that a bearer token was included in the request:
```
I1010 11:04:34.218270   38504 round_trippers.go:466] curl -v -XGET  -H "Accept: application/json;as=Table;v=v1;g=meta.k8s.io,application/json;as=Table;v=v1beta1;g=meta.k8s.io,application/json" -H "User-Agent: kubectl/v1.26.9 (linux/amd64) kubernetes/d1483fd" -H "Authorization: Bearer <masked>" 'https://192.168.39.137:8443/api/v1/namespaces/test-no-access/pods?limit=500'
```

The following line shows the error returned from the kube-apiserver:
```
I1010 11:04:34.222085   38504 helpers.go:246] server response object: [{
  "kind": "Status",
  "apiVersion": "v1",
  "metadata": {},
  "status": "Failure",
  "message": "pods is forbidden: User \"myuser@localhost.localdomain\" cannot list resource \"pods\" in API group \"\" in the namespace \"test-no-access\"",
  "reason": "Forbidden",
  "details": {
    "kind": "pods"
  },
  "code": 403
}]
```

### kube-apiserver

Looking at the log for the `kube-apiserver` is where you will see what happens around authentication on the server-side.

For example the following line shows that the token included the wrong audience:
```
{"log":"E1010 08:51:44.836974       1 authentication.go:70] \"Unable to authenticate the request\" err=\"[invalid bearer token, oidc: verify token: oidc: expected audience \\\"minikube\\\" got [\\\"kubectl\\\"]]\"\n","stream":"stderr","time":"2023-10-10T08:51:44.837047594Z"}
```

To access these logs run `minikube ssh`. The log will be in `/var/log/pods/` in a directory named something like `kube-system_kube-apiserver-minikube_663ddc55f189bfce98a72a82602b90d3`. 

If you've had `minikube` running for a while, or have restarted it several times there will be several logs. Here's a 
one-liner to get you the latest log:

```
minikube ssh 'su -l'
cd /var/log/pods && cd $(ls -t | grep kube-apiserver | head -n 1)/kube-apiserver && ls -t | head -n 1 | xargs cat
```