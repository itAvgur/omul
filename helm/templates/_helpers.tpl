{{/*
Expand the name of the chart.
*/}}
{{- define "omul.name" -}}
{{- default .Chart.Name .Values.nameOverride | trunc 63 | trimSuffix "-" }}
{{- end }}

{{/*
Create a default fully qualified app name.
We truncate at 63 chars because some Kubernetes name fields are limited to this (by the DNS naming spec).
If release name contains chart name it will be used as a full name.
*/}}
{{- define "omul.fullname" -}}
{{- if .Values.fullnameOverride }}
{{- .Values.fullnameOverride | trunc 63 | trimSuffix "-" }}
{{- else }}
{{- $name := default .Chart.Name .Values.nameOverride }}
{{- if contains $name .Release.Name }}
{{- .Release.Name | trunc 63 | trimSuffix "-" }}
{{- else }}
{{- printf "%s-%s" .Release.Name $name | trunc 63 | trimSuffix "-" }}
{{- end }}
{{- end }}
{{- end }}

{{/*
Create chart name and version as used by the chart label.
*/}}
{{- define "omul.chart" -}}
{{- printf "%s-%s" .Chart.Name .Chart.Version | replace "+" "_" | trunc 63 | trimSuffix "-" }}
{{- end }}

{{/*
Common labels
*/}}
{{- define "omul.labels" -}}
helm.sh/chart: {{ include "omul.chart" . }}
{{ include "omul.selectorLabels" . }}
{{- if .Chart.AppVersion }}
app.kubernetes.io/version: {{ .Chart.AppVersion | quote }}
{{- end }}
app.kubernetes.io/managed-by: {{ .Release.Service }}
{{- end }}

{{/*
Selector labels
*/}}
{{- define "omul.selectorLabels" -}}
app.kubernetes.io/name: {{ include "omul.name" . }}
app.kubernetes.io/instance: {{ .Release.Name }}
{{- end }}

{{/*
Create the name of the service account to use
*/}}
{{- define "omul.serviceAccountName" -}}
{{- if .Values.serviceAccount.create }}
{{- default (include "omul.fullname" .) .Values.serviceAccount.name }}
{{- else }}
{{- default "default" .Values.serviceAccount.name }}
{{- end }}
{{- end }}

{{/*
Create the name of the service account to use
*/}}
{{- define "auth.serviceAccountName" -}}
{{- if .Values.serviceAccount.create }}
{{- default (include "omul.fullname" .) .Values.serviceAccount.name }}
{{- else }}
{{- default "default" .Values.serviceAccount.name }}
{{- end }}
{{- end }}

{{- define "auth.labels" -}}
helm.sh/chart: {{ include "omul.chart" . }}
{{ include "omul.labels" . }}
{{ include "auth.selectorLabels" . }}
{{- if .Chart.AppVersion }}
app.kubernetes.io/version: {{ .Chart.AppVersion | quote }}
{{- end }}
app.kubernetes.io/managed-by: {{ .Release.Service }}
{{- end }}
{{- define "auth.selectorLabels" -}}
app.kubernetes.io/name: {{.Values.authService.name}}
app.kubernetes.io/instance: {{ .Release.Name }}
{{- end }}

{{- define "customer.labels" -}}
helm.sh/chart: {{ include "omul.chart" . }}
{{ include "omul.labels" . }}
{{ include "customer.selectorLabels" . }}
{{- if .Chart.AppVersion }}
app.kubernetes.io/version: {{ .Chart.AppVersion | quote }}
{{- end }}
app.kubernetes.io/managed-by: {{ .Release.Service }}
{{- end }}
{{- define "customer.selectorLabels" -}}
app.kubernetes.io/name: {{.Values.customerService.name}}
app.kubernetes.io/instance: {{ .Release.Name }}
{{- end }}

{{- define "schedule.labels" -}}
helm.sh/chart: {{ include "omul.chart" . }}
{{ include "omul.labels" . }}
{{ include "schedule.selectorLabels" . }}
{{- if .Chart.AppVersion }}
app.kubernetes.io/version: {{ .Chart.AppVersion | quote }}
{{- end }}
app.kubernetes.io/managed-by: {{ .Release.Service }}
{{- end }}
{{- define "schedule.selectorLabels" -}}
app.kubernetes.io/name: {{.Values.scheduleService.name}}
app.kubernetes.io/instance: {{ .Release.Name }}
{{- end }}

{{- define "personnel.labels" -}}
helm.sh/chart: {{ include "omul.chart" . }}
{{ include "omul.labels" . }}
{{ include "personnel.selectorLabels" . }}
{{- if .Chart.AppVersion }}
app.kubernetes.io/version: {{ .Chart.AppVersion | quote }}
{{- end }}
app.kubernetes.io/managed-by: {{ .Release.Service }}
{{- end }}
{{- define "personnel.selectorLabels" -}}
app.kubernetes.io/name: {{.Values.personnelService.name}}
app.kubernetes.io/instance: {{ .Release.Name }}
{{- end }}

{{- define "appointment.labels" -}}
helm.sh/chart: {{ include "omul.chart" . }}
{{ include "omul.labels" . }}
{{ include "appointment.selectorLabels" . }}
{{- if .Chart.AppVersion }}
app.kubernetes.io/version: {{ .Chart.AppVersion | quote }}
{{- end }}
app.kubernetes.io/managed-by: {{ .Release.Service }}
{{- end }}
{{- define "appointment.selectorLabels" -}}
app.kubernetes.io/name: {{.Values.appointmentService.name}}
app.kubernetes.io/instance: {{ .Release.Name }}
{{- end }}

{{- define "postgres.fullTag" -}}
{{- printf "%s:%s" "postgresql" .Values.postgresql.image.tag  | trimSuffix "-" }}
{{- end }}

{{- define "omul.monitorable" -}}
kubernetes/monitor-able: "true"
{{- end }}