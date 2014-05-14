package com.fatboycentral.shared;

import java.util.LinkedList;
import java.util.List;

import com.google.gwt.user.client.rpc.IsSerializable;

public class SignupData {

	public static class SchemaJob implements IsSerializable {
		public String job;
		public String description;
	}
	
	public static class Shift implements IsSerializable {
		// The unique identifier of this job: subtitle + description
		public String jid;
		// the jid of the master job (i.e. this is an assisstant)
		public String master;
		// The coordinator level job (e.g. Hydration)
		public String job;
		// The description of the job (e.g. 3:00pm - 4:00pm)		
		public String description; 
		// The special instructions for this job
		public String instructions; 
		// The number of slots
		public int slots;
		// Who can signup?
		public boolean adminOnly;
	}

	public static class Person implements IsSerializable {
		public String name; 
		public String email;
		public String url;
		public String comment;
		// Who did signup?
		public long cookie;

		@Override 
		public int hashCode() {
			return (name + email).hashCode();
		}
		
		@Override 
		public boolean equals(Object o) {
			if (o == null) return false;
			if (this == o) return true;
			if (getClass() != o.getClass()) return false;
			Person other = (Person) o;
			return (name.equals(other.name) && email.equals(other.email));
		}
	}
	
	public static class DataStoreEntry implements IsSerializable {
		public long id = 0; 
		public Shift shift = new Shift(); 
		public Person worker = new Person();
		
		@Override 
		public int hashCode() {
			return (shift.job + shift.jid + id).hashCode();
		}

		@Override 
		public boolean equals(Object o) {
			if (o == null) return false;
			if (this == o) return true;
			if (getClass() != o.getClass()) return false;
			DataStoreEntry other = (DataStoreEntry) o;
			return (shift.job.equals(other.shift.job) && shift.jid.equals(other.shift.jid) && id == other.id);
		}
	}
	
	public static class SignupEntry implements IsSerializable {
		public Shift shift = new Shift();
		public int slots = 0; 
		
		public List<DataStoreEntry> entries = new LinkedList<DataStoreEntry>();

		public SignupEntry() {
		}
		
		public SignupEntry(SignupEntry other, Person p) {
			this.shift = other.shift;
			this.slots = other.slots;
			
			this.entries.addAll(other.entries);
			DataStoreEntry ne = new DataStoreEntry();
			ne.shift = this.shift;
			ne.worker = p;
			this.entries.add(ne);
		}

		public SignupEntry(SignupEntry other) {
			this.shift = other.shift;
			this.slots = other.slots;
			this.entries.addAll(other.entries);
		}
	}

	public static class SchemaEntry implements IsSerializable {
		public int order;
		public String key; 
		public String data;
		
		@Override 
		public int hashCode() {
			return key.hashCode();
		}

		@Override 
		public boolean equals(Object other) {
			if (other == null) return false;
			if (this == other) return true;
			if (getClass() != other.getClass()) return false;
			return (key.equals(((SchemaEntry) other).key));
		}

	}
	
	public static class Database implements IsSerializable {
		public List<SchemaEntry> schema; 
		public List<DataStoreEntry> entries; 		
		
	}
	
	public static class JobStats implements IsSerializable {
		public int total = 0;
		public int taken = 0;
	}
}
