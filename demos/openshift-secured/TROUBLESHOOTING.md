### Config

#### Check oauth-meta configmap

```
oc get configmap oauth-meta -n openshift-config -o json
```

####  Check OpenShift cluster authentication config

```
oc get authentication cluster -o json
```

#### Check kube-apiserver config

```
oc get kubeapiserver cluster -o json
```

#### Check configuration revisions

```
oc get co kube-apiserver
```