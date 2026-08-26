import React, { useState } from 'react';
import { View, Text, TextInput, TouchableOpacity, StyleSheet, SafeAreaView } from 'react-native';
import { useStore } from '../store/useStore';

export default function LoginScreen({ navigation }: any) {
  const [email, setEmail] = useState('operator@apex.com');
  const setUser = useStore((state) => state.setUser);

  const handleLogin = (role: string) => {
    const userProfile = {
      email,
      fullName: role === 'ROLE_OPERATOR' ? 'Amir Khan (Operator)' : role === 'ROLE_FACTORY_OWNER' ? 'Omprakash Singh (Owner)' : 'Warehouse Specialist',
      role
    };
    setUser(userProfile);
  };

  return (
    <SafeAreaView style={styles.container}>
      <View style={styles.content}>
        <Text style={styles.title}>MfgOS Mobile</Text>
        <Text style={styles.subtitle}>Enterprise Factory Operations Console</Text>

        <View style={styles.formGroup}>
          <Text style={styles.label}>Operator Email / Badge Code</Text>
          <TextInput
            style={styles.input}
            value={email}
            onChangeText={setEmail}
            placeholder="Enter ID..."
          />
        </View>

        <Text style={styles.sectionLabel}>Select Quick Persona Terminal</Text>

        <TouchableOpacity 
          style={styles.primaryButton}
          onPress={() => handleLogin('ROLE_OPERATOR')}
        >
          <Text style={styles.primaryButtonText}>Log In as Worker Operator</Text>
        </TouchableOpacity>

        <TouchableOpacity 
          style={styles.secondaryButton}
          onPress={() => handleLogin('ROLE_QUALITY_INSPECTOR')}
        >
          <Text style={styles.secondaryButtonText}>Log In as QC & Warehouse Team</Text>
        </TouchableOpacity>

        <TouchableOpacity 
          style={styles.outlineButton}
          onPress={() => handleLogin('ROLE_FACTORY_OWNER')}
        >
          <Text style={styles.outlineButtonText}>Log In as Factory Owner</Text>
        </TouchableOpacity>
      </View>
    </SafeAreaView>
  );
}

const styles = StyleSheet.create({
  container: { flex: 1, backgroundColor: '#f8fafc' },
  content: { flex: 1, padding: 24, justifyContent: 'center' },
  title: { fontSize: 24, fontWeight: '800', color: '#0f172a', textTransform: 'uppercase' },
  subtitle: { fontSize: 13, color: '#64748b', marginTop: 4, marginBottom: 32 },
  formGroup: { marginBottom: 20 },
  label: { fontSize: 12, fontWeight: '700', color: '#475569', marginBottom: 6 },
  input: { backgroundColor: '#ffffff', borderWidth: 1, borderColor: '#cbd5e1', borderRadius: 10, padding: 12, fontSize: 14, color: '#0f172a' },
  sectionLabel: { fontSize: 11, fontWeight: '700', color: '#94a3b8', textTransform: 'uppercase', marginBottom: 12, marginTop: 12 },
  primaryButton: { backgroundColor: '#4f46e5', padding: 16, borderRadius: 12, alignItems: 'center', marginBottom: 10 },
  primaryButtonText: { color: '#ffffff', fontWeight: '800', fontSize: 14 },
  secondaryButton: { backgroundColor: '#0284c7', padding: 16, borderRadius: 12, alignItems: 'center', marginBottom: 10 },
  secondaryButtonText: { color: '#ffffff', fontWeight: '800', fontSize: 14 },
  outlineButton: { borderHeight: 1, borderColor: '#cbd5e1', padding: 16, borderRadius: 12, alignItems: 'center', backgroundColor: '#ffffff' },
  outlineButtonText: { color: '#334155', fontWeight: '800', fontSize: 14 },
});
