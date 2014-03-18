package sie.metrics;

import sie.db.entity.Project;

public interface IMetric {
	public void compute(Project p);
}
