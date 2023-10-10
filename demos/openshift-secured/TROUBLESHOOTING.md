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

### Logs

#### kube-apiserver

```
oc logs $(oc get pods -n openshift-kube-apiserver | grep kube-apiserver | cut -f 1 -d ' ') -n openshift-kube-apiserver
```

#### OpenShift console

```
oc logs $(oc get pods -n openshift-console | grep console | cut -f 1 -d ' ') -n openshift-console
```
