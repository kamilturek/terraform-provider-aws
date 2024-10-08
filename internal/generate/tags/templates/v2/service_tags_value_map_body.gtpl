// map[string]string handling

// {{ .TagsFunc }} returns {{ .ServicePackage }} service tags.
func {{ .TagsFunc }}(tags tftags.KeyValueTags) map[string]string {
	return tags.Map()
}

// {{ .KeyValueTagsFunc }} creates tftags.KeyValueTags from {{ .ServicePackage }} service tags.
func {{ .KeyValueTagsFunc }}(ctx context.Context, tags map[string]string) tftags.KeyValueTags {
	return tftags.New(ctx, tags)
}

// {{ .GetTagsInFunc }} returns {{ .ServicePackage }} service tags from Context.
// nil is returned if there are no input tags.
func {{ .GetTagsInFunc }}(ctx context.Context) map[string]string {
	if inContext, ok := tftags.FromContext(ctx); ok {
		if tags := {{ .TagsFunc }}(inContext.TagsIn.UnwrapOrDefault()); len(tags) > 0 {
			return tags
		}
	}

{{ if .EmptyMap -}}
	return map[string]string{}
{{- else -}}
	return nil
{{- end }}
}

// {{ .SetTagsOutFunc }} sets {{ .ServicePackage }} service tags in Context.
func {{ .SetTagsOutFunc }}(ctx context.Context, tags map[string]string) {
	if inContext, ok := tftags.FromContext(ctx); ok {
		inContext.TagsOut = option.Some({{ .KeyValueTagsFunc }}(ctx, tags))
	}
}

{{- if ne .CreateTagsFunc "" }}
// {{ .CreateTagsFunc }} creates {{ .ServicePackage }} service tags for new resources.
func {{ .CreateTagsFunc }}(ctx context.Context, conn {{ .ClientType }}, identifier{{ if .TagResTypeElem }}, resourceType{{ end }} string, tags map[string]string, optFns ...func(*{{ .AWSService }}.Options)) error {
	if len(tags) == 0 {
		return nil
	}

	return  {{ .UpdateTagsFunc }}(ctx, conn, identifier{{ if .TagResTypeElem }}, resourceType{{ end }}, nil, tags, optFns...)
}
{{- end }}
